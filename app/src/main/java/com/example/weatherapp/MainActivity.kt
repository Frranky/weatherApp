package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.domain.usecase.getCityName
import com.example.weatherapp.domain.usecase.getData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var data: ArrayList<ForecastModel>
	private var searchFlag = false

	private var formatter: ValueFormatter = object : ValueFormatter() {
		override fun getAxisLabel(value: Float, axis: AxisBase): String {
			return SimpleDateFormat("MM.dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data[value.toInt()].date))
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		supportFragmentManager.beginTransaction().add(R.id.container, PreLoaderFragment()).commit()
		val context = this.baseContext

		GlobalScope.launch {
			val name = getCityName(context)
			val response = getData(context, name)
			data = response.first
			val fetchDate = SimpleDateFormat("MM.dd HH:mm").format(response.second)
			val entries = arrayListOf<Entry>()

			for (i in 0 until data.size) {
				entries.add(Entry(i.toFloat(), data[i].temp.toFloat()))
			}
			val dataset = LineDataSet(entries, "Temperature")
			dataset.axisDependency = YAxis.AxisDependency.LEFT

			launch(Dispatchers.Main) {
				binding.container.visibility = View.GONE
				binding.navView.visibility = View.VISIBLE
				binding.toolbar.textView.text = name
				binding.chart.data = LineData(dataset)
				binding.chart.xAxis.valueFormatter = formatter
				binding.chart.xAxis.granularity = 1f
				binding.chart.xAxis.textSize = 5f
				binding.chart.axisLeft.setDrawGridLines(false)
				binding.chart.description.text = ""
				binding.fetchText.text = "Last fetched: $fetchDate"
				binding.chart.invalidate()
			}
		}

		binding.navView.setOnItemSelectedListener {
			when (it.itemId) {
				R.id.temperature -> {
					setTempChart("Temperature")
					true
				}

				R.id.wind        -> {
					setTempChart("Wind (m/sec.)")
					true
				}

				R.id.humidity    -> {
					setTempChart("Humidity (%)")
					true
				}

				else             -> true
			}
		}

		binding.toolbar.information.setOnClickListener {
			val myDialogFragment = AboutDialogFragment()
			val manager = supportFragmentManager
			val transaction: FragmentTransaction = manager.beginTransaction()
			myDialogFragment.show(transaction, "dialog")
		}

		binding.toolbar.explore.setOnClickListener {
			if (!searchFlag) {
				searchFlag = true
				binding.searchBar.visibility = View.VISIBLE
				binding.searchBar.startAnimation(AnimationUtils.loadAnimation(context, R.anim.down))
				return@setOnClickListener
			}
			searchFlag = false
			binding.searchBar.visibility = View.INVISIBLE
			binding.searchBar.startAnimation(AnimationUtils.loadAnimation(context, R.anim.up))
		}

		binding.searchButton.setOnClickListener {
			fetchData(binding.searchText.text.toString())
		}
		setContentView(binding.root)
	}

	private fun setTempChart(name: String) {
		val entries = data.mapIndexed { index, forecastModel ->
			Entry(
				index.toFloat(),
				when (name) {
					"Temperature"   -> forecastModel.temp.toFloat()
					"Wind (m/sec.)" -> forecastModel.wind_speed.toFloat()
					"Humidity (%)"  -> forecastModel.humidity.toFloat()
					else            -> throw IllegalArgumentException("Unknown parameter name:$name")
				}
			)
		}

		val dataset = LineDataSet(entries, name)
		dataset.axisDependency = YAxis.AxisDependency.LEFT
		binding.chart.data = LineData(dataset)
		binding.chart.xAxis.valueFormatter = formatter
		binding.chart.xAxis.granularity = 1f
		binding.chart.xAxis.textSize = 5f
		binding.chart.axisLeft.setDrawGridLines(false)
		binding.chart.invalidate()
	}

	private fun fetchData(name: String) {
		val context = this.baseContext

		GlobalScope.launch {
			val response = getData(context, name, false)
			data = response.first
			val fetchDate = SimpleDateFormat("MM.dd HH:mm").format(response.second)
			val entries = arrayListOf<Entry>()

			for (i in 0 until data.size) {
				entries.add(Entry(i.toFloat(), data[i].temp.toFloat()))
			}
			val dataset = LineDataSet(entries, "Temperature")
			dataset.axisDependency = YAxis.AxisDependency.LEFT

			launch(Dispatchers.Main) {
				binding.chart.data = LineData(dataset)
				binding.fetchText.text = "Last fetched: $fetchDate"
				binding.toolbar.textView.text = name
				binding.chart.invalidate()
			}
		}
	}
}
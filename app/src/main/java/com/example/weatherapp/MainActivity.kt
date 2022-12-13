package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.databinding.ActivityMainBinding
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
import java.util.Date

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	internal lateinit var data: ArrayList<ForecastModel>

	private var formatter: ValueFormatter = object : ValueFormatter() {
		override fun getAxisLabel(value: Float, axis: AxisBase): String {
			return SimpleDateFormat("MM.dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data[value.toInt()].date))
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		supportFragmentManager.beginTransaction().add(R.id.container, PreLoaderFragment()).commit()
		val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())).time
		val context = this.baseContext

		GlobalScope.launch {
			val response = getData(currentDate, context)
			data = response.first
			val fetchDate = SimpleDateFormat("MM.dd HH:mm").format(response.second)
			val entries = arrayListOf<Entry>()

			for(i in 0 until data.size) {
				entries.add(Entry(i.toFloat(), data[i].temp.toFloat()))
			}
			val dataset = LineDataSet(entries, "Temperature")
			dataset.axisDependency = YAxis.AxisDependency.LEFT

			launch(Dispatchers.Main) {
				binding.container.visibility = View.GONE
				binding.navView.visibility = View.VISIBLE
				binding.chart.data = LineData(dataset)
				binding.chart.xAxis.valueFormatter = formatter
				binding.chart.xAxis.granularity = 1f
				binding.chart.xAxis.textSize = 5f
				binding.chart.axisLeft.setDrawGridLines(false)
				binding.chart.description.text = "Last fetched: $fetchDate"
				binding.chart.invalidate()
			}
		}

		binding.navView.setOnItemSelectedListener {
			when(it.itemId) {
				R.id.temperature 	-> {
					setTempChart("Temperature")
					true
				}
				R.id.wind 			-> {
					setTempChart("Wind (m/sec.)")
					true
				}
				R.id.humidity 			-> {
					setTempChart("Humidity (%)")
					true
				}
				else 				-> true
			}
		}

		binding.toolbar.information.setOnClickListener {
			val myDialogFragment = AboutDialogFragment()
			val manager = supportFragmentManager
			val transaction: FragmentTransaction = manager.beginTransaction()
			myDialogFragment.show(transaction, "dialog")
		}
		setContentView(binding.root)
	}

	private fun setTempChart(name: String) {
		val entries = arrayListOf<Entry>()

		when(name) {
			"Temperature" ->
				for(i in 0 until data.size) {
					entries.add(Entry(i.toFloat(), data[i].temp.toFloat()))
				}
			"Wind (m/sec.)" ->
				for(i in 0 until data.size) {
					entries.add(Entry(i.toFloat(), data[i].wind_speed.toFloat()))
				}
			"Humidity (%)" ->
				for(i in 0 until data.size) {
					entries.add(Entry(i.toFloat(), data[i].humidity.toFloat()))
				}
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
}
package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.mapper.toEntry
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.domain.usecase.GetCityNameUseCase
import com.example.weatherapp.domain.usecase.GetDataUseCase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("SimpleDateFormat", "StaticFieldLeak", "SetTextI18n")
class MainActivityViewModel(
	private val binding: ActivityMainBinding,
	private val context: Context,
) : ViewModel() {

	private lateinit var data: ArrayList<ForecastModel>

	private val getCityNameUseCase = GetCityNameUseCase()
	private val getDataUseCase = GetDataUseCase()

	private var formatter: ValueFormatter = object : ValueFormatter() {
		override fun getAxisLabel(value: Float, axis: AxisBase): String {
			return SimpleDateFormat("MM.dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data[value.toInt()].date)!!)
		}
	}

	init {
		GlobalScope.launch {
			val name = getCityNameUseCase(context)
			val response = getDataUseCase(context, name)
			data = response.data
			val fetchDate = SimpleDateFormat("MM.dd HH:mm").format(response.timestamp)
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
	}

	fun fetchData(name: String) {
		GlobalScope.launch {
			val response = getDataUseCase(context, name, false)
			data = response.data
			val fetchDate = SimpleDateFormat("MM.dd HH:mm").format(response.timestamp)
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

	fun setTempChart(name: String) {
		val dataset = LineDataSet(toEntry(name, data), name)
		dataset.axisDependency = YAxis.AxisDependency.LEFT
		binding.chart.data = LineData(dataset)
		binding.chart.xAxis.valueFormatter = formatter
		binding.chart.xAxis.granularity = 1f
		binding.chart.xAxis.textSize = 5f
		binding.chart.axisLeft.setDrawGridLines(false)
		binding.chart.invalidate()
	}
}

class MainActivityViewModelFactory(
	private val binding: ActivityMainBinding,
	private val context: Context,
) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return MainActivityViewModel(binding, context) as T
	}
}
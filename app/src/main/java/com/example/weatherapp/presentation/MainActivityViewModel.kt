package com.example.weatherapp.presentation

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.domain.model.ForecastModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.domain.usecase.GetCityNameUseCase
import com.example.weatherapp.domain.usecase.GetWeatherForecastUseCase
import com.example.weatherapp.presentation.mapper.EntryMapper
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
import java.util.Date

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("SimpleDateFormat", "StaticFieldLeak", "SetTextI18n")
class MainActivityViewModel(
	private val binding: ActivityMainBinding,
	private val getCityNameUseCase: GetCityNameUseCase,
	private val getDataUseCase: GetWeatherForecastUseCase,
) : ViewModel() {

	private lateinit var data: ArrayList<ForecastModel>

	private val entryMapper = EntryMapper()

	private var formatter: ValueFormatter = object : ValueFormatter() {
		override fun getAxisLabel(value: Float, axis: AxisBase): String {
			return SimpleDateFormat("MM.dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data[value.toInt()].date)!!)
		}
	}

	init {
		GlobalScope.launch {
			val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))!!.time
			val name = getCityNameUseCase()
			val response = getDataUseCase(name, currentDate)
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
			val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))!!.time
			val response = getDataUseCase(name, currentDate, false)
			if (response.timestamp == (-1).toLong()) return@launch
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
		val dataset = LineDataSet(entryMapper(name, data), name)
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
	private val getCityNameUseCase: GetCityNameUseCase,
	private val getDataUseCase: GetWeatherForecastUseCase,
) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return MainActivityViewModel(binding, getCityNameUseCase, getDataUseCase) as T
	}
}
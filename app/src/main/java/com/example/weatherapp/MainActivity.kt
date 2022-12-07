package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.data.api.getWeather
import com.example.weatherapp.data.mapper.toForecastModel
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
	private lateinit var data: ArrayList<ForecastModel>

	private var formatter: ValueFormatter = object : ValueFormatter() {
		override fun getAxisLabel(value: Float, axis: AxisBase): String {
			return SimpleDateFormat("MM.dd HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data[value.toInt()].date))
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())).time
		val context = this.baseContext

		GlobalScope.launch {
			data = getData(currentDate, context)
			val entries = arrayListOf<Entry>()

			for(i in 0 until data.size) {
				entries.add(Entry(i.toFloat(), data[i].temp.toFloat()))
			}
			val dataset = LineDataSet(entries, "Temperature")
			dataset.axisDependency = YAxis.AxisDependency.LEFT

			launch(Dispatchers.Main) {
				binding.chart.data = LineData(dataset)
				binding.chart.xAxis.valueFormatter = formatter
				binding.chart.invalidate()
			}
		}
		setContentView(binding.root)
	}
}
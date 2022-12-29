package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.ForecastModel
import com.github.mikephil.charting.data.Entry

fun toEntry(name: String, data: ArrayList<ForecastModel>): List<Entry> =
	data.mapIndexed { index, forecastModel ->
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
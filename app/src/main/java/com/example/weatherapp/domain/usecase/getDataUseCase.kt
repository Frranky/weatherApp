package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.api.getGeocode
import com.example.weatherapp.data.api.getWeather
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.mapper.toGeocodeModel
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.ResponseModel
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

fun getData(context: Context, name: String, flag: Boolean = true): ResponseModel {
	val path = context.filesDir
	val letDirectory = File(path, "json")
	val file = File(letDirectory, "data.json")
	val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())).time

	if (file.exists()) {
		val cash = toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })

		if (currentDate - cash.timestamp < TimeUnit.MINUTES.toMillis(10) && flag)
			return cash
	}

	val geocode = toGeocodeModel(getGeocode(name))
	val data = toForecastModel(getWeather(geocode.lat, geocode.lon))
	saveData(name, data, currentDate, context)
	return ResponseModel(data, currentDate)
}

fun getCityName(context: Context): String {
	val path = context.filesDir
	val letDirectory = File(path, "json")
	val file = File(letDirectory, "city.json")

	if (file.exists()) {
		return FileInputStream(file).bufferedReader().use { it.readText() }
	}
	return "Moscow"
}

private fun saveData(city: String, data: ArrayList<ForecastModel>, currentDate: Long, context: Context) {
	val json = Gson().toJson(data to currentDate)
	val path = context.filesDir
	val letDirectory = File(path, "json")
	letDirectory.mkdirs()
	val file = File(letDirectory, "data.json")
	val cityFile = File(letDirectory, "city.json")
	file.writeText(json)
	cityFile.writeText(city)
}
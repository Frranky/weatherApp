package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.api.getGeocode
import com.example.weatherapp.data.api.getWeather
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.mapper.toGeocodeModel
import com.example.weatherapp.data.model.ForecastModel
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileInputStream

fun getData(currentDate: Long, context: Context, name: String, flag: Boolean = true): Pair<ArrayList<ForecastModel>, Long> {
	val path = context.filesDir
	val letDirectory = File(path, "json")
	letDirectory.mkdirs()
	val file = File(letDirectory, "data.json")
	val cash = toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })
	val geocode = toGeocodeModel(getGeocode(name))

	if(currentDate - cash.second < 600 && flag)
		return toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })

	val data = toForecastModel(getWeather(geocode.lat, geocode.lon))
	saveData(data, currentDate, file)
	return data to currentDate
}

private fun saveData(data: ArrayList<ForecastModel>, currentDate: Long, file: File) {
	val gsonBuilder = GsonBuilder()
	val gson = gsonBuilder.create()
	val json = gson.toJson(data to currentDate)
	file.writeText(json)
}
package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.api.getWeather
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.model.ForecastModel
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileInputStream

fun getData(currentDate: Long, context: Context): Pair<ArrayList<ForecastModel>, Long> {
	val path = context.filesDir
	val letDirectory = File(path, "json")
	letDirectory.mkdirs()
	val file = File(letDirectory, "data.json")
	val cash = toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })

	if(currentDate - cash.second < 600)
		return toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })

	val data = toForecastModel(getWeather())
	saveData(data, currentDate, context)
	return data to currentDate
}

private fun saveData(data: ArrayList<ForecastModel>, currentDate: Long, context: Context) {
	val gsonBuilder = GsonBuilder()
	val gson = gsonBuilder.create()
	val json = gson.toJson(data to currentDate)
	val path = context.filesDir
	val letDirectory = File(path, "json")
	letDirectory.mkdirs()
	val file = File(letDirectory, "data.json")
	file.writeText(json)
}
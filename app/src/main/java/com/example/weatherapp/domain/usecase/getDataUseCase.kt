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

	if(currentDate - file.lastModified() < 600)
		return toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() }) to file.lastModified()

	val data = toForecastModel(getWeather())
	saveData(data, context)
	return data to currentDate
}

private fun saveData(data: ArrayList<ForecastModel>, context: Context) {
	val gsonBuilder = GsonBuilder()
	val gson = gsonBuilder.create()
	val json = gson.toJson(data)
	val path = context.filesDir
	val letDirectory = File(path, "json")
	letDirectory.mkdirs()
	val file = File(letDirectory, "data.json")
	file.writeText(json)
}
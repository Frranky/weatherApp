package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.domain.repository.CityRepository
import com.example.weatherapp.domain.repository.WeatherForecastRepository
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream

class LocalDataRepository: CityRepository, WeatherForecastRepository {

	override fun city(context: Context): String {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "city.json")

		if (file.exists()) {
			return FileInputStream(file).bufferedReader().use { it.readText() }
		}
		return "Moscow"
	}

	override fun weatherForecast(context: Context): String {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "data.json")

		if (file.exists())
			return FileInputStream(file).bufferedReader().use { it.readText() }
		return "none"
	}

	fun saveData(city: String, data: ArrayList<ForecastModel>, currentDate: Long, context: Context) {
		val json = Gson().toJson(data to currentDate)
		val path = context.filesDir
		val letDirectory = File(path, "json")
		letDirectory.mkdirs()
		val file = File(letDirectory, "data.json")
		val cityFile = File(letDirectory, "city.json")
		file.writeText(json)
		cityFile.writeText(city)
	}


}
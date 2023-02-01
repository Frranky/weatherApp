package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.data.api.GeocodeApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.mapper.toGeocodeModel
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.ResponseModel
import com.example.weatherapp.domain.repository.CityRepository
import com.example.weatherapp.domain.repository.WeatherForecastRepository
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

class LocalDataRepository : CityRepository, WeatherForecastRepository {

	private val weatherApi = WeatherApi()
	private val geocodeApi = GeocodeApi()

	override fun city(context: Context): String {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "city.json")

		if (file.exists()) {
			return FileInputStream(file).bufferedReader().use { it.readText() }
		}
		return "Moscow"
	}

	override fun weatherForecast(context: Context, name: String, currentDate: Long, flag: Boolean): ResponseModel {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "data.json")

		if (file.exists()) {
			val cash = toForecastModel(FileInputStream(file).bufferedReader().use { it.readText() })

			if (currentDate - cash.timestamp < TimeUnit.MINUTES.toMillis(10) && flag)
				return cash
		}

		val geocode = toGeocodeModel(geocodeApi.get(name))
		val data = toForecastModel(weatherApi.get(geocode.lat, geocode.lon))
		saveData(name, data, currentDate, context)
		return ResponseModel(data, currentDate)
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
}
package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.data.api.GeocodeApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.ForecastMapper
import com.example.weatherapp.data.mapper.GeocodeMapper
import com.example.weatherapp.domain.model.ForecastModel
import com.example.weatherapp.data.model.ResponseModel
import com.example.weatherapp.domain.repository.ICityRepository
import com.example.weatherapp.domain.repository.IWeatherForecastRepository
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

class LocalDataRepository(private val context: Context) : ICityRepository, IWeatherForecastRepository {

	private val weatherApi = WeatherApi()
	private val geocodeApi = GeocodeApi()
	private val forecastMapper = ForecastMapper()
	private val geocodeMapper = GeocodeMapper()

	override fun city(): String {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "city.json")

		if (file.exists()) {
			return FileInputStream(file).bufferedReader().use { it.readText() }
		}
		return "Moscow"
	}

	override fun weatherForecast(name: String, currentDate: Long, flag: Boolean): ResponseModel {
		try {
			val path = context.filesDir
			val letDirectory = File(path, "json")
			val file = File(letDirectory, "data.json")

			if (file.exists()) {
				val cash = forecastMapper(FileInputStream(file).bufferedReader().use { it.readText() })

				if (currentDate - cash.timestamp < TimeUnit.MINUTES.toMillis(10) && flag)
					return cash
			}

			val geocode = geocodeMapper(geocodeApi.get(name))
			val data = forecastMapper(weatherApi.get(geocode.lat, geocode.lon))
			saveData(name, data, currentDate, context)
			return ResponseModel(data, currentDate)
		}
		catch (e: Exception) {
			return ResponseModel(arrayListOf(), -1)
		}
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
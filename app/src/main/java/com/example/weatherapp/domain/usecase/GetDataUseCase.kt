package com.example.weatherapp.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import com.example.weatherapp.data.api.GeocodeApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.mapper.toGeocodeModel
import com.example.weatherapp.data.model.ResponseModel
import com.example.weatherapp.domain.repositiry.LocalDataRepository
import java.util.concurrent.TimeUnit

class GetDataUseCase {

	private val weatherApi = WeatherApi()
	private val geocodeApi = GeocodeApi()
	private val localDataRepository = LocalDataRepository()

	@SuppressLint("SimpleDateFormat")
	operator fun invoke(context: Context, name: String, currentDate: Long, flag: Boolean = true): ResponseModel {
		val fileData = localDataRepository.getData(context)

		if (fileData != "none") {
			val cash = toForecastModel(fileData)

			if (currentDate - cash.timestamp < TimeUnit.MINUTES.toMillis(10) && flag)
				return cash
		}

		val geocode = toGeocodeModel(geocodeApi.get(name))
		val data = toForecastModel(weatherApi.get(geocode.lat, geocode.lon))
		localDataRepository.saveData(name, data, currentDate, context)
		return ResponseModel(data, currentDate)
	}
}
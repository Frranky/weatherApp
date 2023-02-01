package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.model.ResponseModel

interface WeatherForecastRepository {

	fun weatherForecast(name: String, currentDate: Long, flag: Boolean = true): ResponseModel
}
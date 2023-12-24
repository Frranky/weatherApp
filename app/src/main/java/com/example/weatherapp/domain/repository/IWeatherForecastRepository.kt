package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.model.ResponseModel

interface IWeatherForecastRepository {

	fun weatherForecast(name: String, currentDate: Long, flag: Boolean = true): ResponseModel
}
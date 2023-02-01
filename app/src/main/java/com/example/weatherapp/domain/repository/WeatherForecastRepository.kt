package com.example.weatherapp.domain.repository

import android.content.Context
import com.example.weatherapp.data.model.ResponseModel

interface WeatherForecastRepository {

	fun weatherForecast(context: Context, name: String, currentDate: Long, flag: Boolean = true): ResponseModel
}
package com.example.weatherapp.domain.repository

import android.content.Context

interface WeatherForecastRepository {

	fun weatherForecast(context: Context): String
}
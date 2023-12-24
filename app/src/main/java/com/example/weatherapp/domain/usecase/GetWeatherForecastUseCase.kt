package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.IWeatherForecastRepository

class GetWeatherForecastUseCase(private val repository: IWeatherForecastRepository) {

	operator fun invoke(name: String, currentDate: Long, flag: Boolean = true) =
		repository.weatherForecast(name, currentDate, flag)
}
package com.example.weatherapp.domain.usecase

import com.example.weatherapp.data.repository.LocalDataRepository

class GetWeatherForecastUseCase(private val localDataRepository: LocalDataRepository) {

	suspend operator fun invoke(name: String, currentDate: Long, flag: Boolean = true) =
		localDataRepository.weatherForecast(name, currentDate, flag)
}
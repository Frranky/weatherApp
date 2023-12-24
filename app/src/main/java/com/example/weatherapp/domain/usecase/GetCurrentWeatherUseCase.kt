package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.ICurrentRepository
import com.example.weatherapp.domain.repository.IWeatherForecastRepository

class GetCurrentWeatherUseCase(private val repository: ICurrentRepository) {

    operator fun invoke(name: String) = repository.currentWeather(name)
}
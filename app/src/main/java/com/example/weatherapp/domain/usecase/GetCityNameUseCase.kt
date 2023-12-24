package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.ICityRepository

class GetCityNameUseCase(private val repository: ICityRepository) {

	operator fun invoke() = repository.city()
}
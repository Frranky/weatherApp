package com.example.weatherapp.domain.usecase

import com.example.weatherapp.data.repository.LocalDataRepository

class GetCityNameUseCase(private val localDataRepository: LocalDataRepository) {

	operator fun invoke() = localDataRepository.city()
}
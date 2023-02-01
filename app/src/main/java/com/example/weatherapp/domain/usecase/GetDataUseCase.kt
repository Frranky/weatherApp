package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.repository.LocalDataRepository

class GetDataUseCase {

	private val localDataRepository = LocalDataRepository()

	operator fun invoke(context: Context, name: String, currentDate: Long, flag: Boolean = true) =
		localDataRepository.weatherForecast(context, name, currentDate, flag)
}
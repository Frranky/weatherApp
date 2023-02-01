package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.repository.LocalDataRepository

class GetCityNameUseCase {

	operator fun invoke(context: Context) = LocalDataRepository().city(context)
}
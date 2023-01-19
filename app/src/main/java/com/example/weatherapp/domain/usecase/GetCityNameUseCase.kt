package com.example.weatherapp.domain.usecase

import android.content.Context
import com.example.weatherapp.data.repositiry.LocalDataRepository

class GetCityNameUseCase {

	operator fun invoke(context: Context) = LocalDataRepository().getCity(context)
}
package com.example.weatherapp.domain.repository

import android.content.Context

interface CityRepository {

	fun city(context: Context): String
}
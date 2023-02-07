package com.example.weatherapp.data.model

import com.example.weatherapp.domain.model.ForecastModel

data class ResponseModel(
	val data: ArrayList<ForecastModel>,
	val timestamp: Long,
)
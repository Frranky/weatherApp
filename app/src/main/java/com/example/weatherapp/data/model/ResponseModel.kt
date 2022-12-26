package com.example.weatherapp.data.model

data class ResponseModel(
	val data: ArrayList<ForecastModel>,
	val timestamp: Long,
)
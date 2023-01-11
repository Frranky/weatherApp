package com.example.weatherapp.data.api

import com.google.gson.JsonArray

interface OpenWeatherApiInterface {

	val key: String
		get() = "e7a53fb43aeccf8fff05234eccf7a50c&"

	fun get(vararg elements: String): JsonArray
}
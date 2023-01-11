package com.example.weatherapp.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonParser.parseString
import okhttp3.OkHttpClient
import okhttp3.Request

class GeocodeApi : OpenWeatherApiInterface {

	override fun get(vararg elements: String): JsonArray {
		val client = OkHttpClient()
		val request = Request.Builder()
			.url("http://api.openweathermap.org/geo/1.0/direct?q=${elements[0]},RU&limit=1&appid=$key")
			.get()
			.build()

		val jsonData = client.newCall(request).execute().body()!!.string()
		return parseString(jsonData).asJsonArray
	}
}
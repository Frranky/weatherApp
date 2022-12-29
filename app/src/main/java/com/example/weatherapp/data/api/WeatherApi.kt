package com.example.weatherapp.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request

fun getWeather(lat: String, lon: String): JsonArray {
	val client = OkHttpClient()
	val request = Request.Builder()
		.url("https://api.openweathermap.org/data/2.5/forecast?" +
				 "lat=$lat&" +
				 "lon=$lon&" +
				 "appid=e7a53fb43aeccf8fff05234eccf7a50c&" +
				 "units=metric")
		.get()
		.build()

	val jsonData = client.newCall(request).execute().body()!!.string()
	return JsonParser.parseString(jsonData).asJsonObject.get("list").asJsonArray
}
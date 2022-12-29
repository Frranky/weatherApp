package com.example.weatherapp.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonParser.parseString
import okhttp3.OkHttpClient
import okhttp3.Request

fun getGeocode(name: String): JsonArray {
	val client = OkHttpClient()
	val request = Request.Builder()
		.url("http://api.openweathermap.org/geo/1.0/direct?q=$name,RU&limit=1&appid=e7a53fb43aeccf8fff05234eccf7a50c")
		.get()
		.build()

	val jsonData = client.newCall(request).execute().body()!!.string()
	return parseString(jsonData).asJsonArray
}
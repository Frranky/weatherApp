package com.example.weatherapp.data.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

fun getGeocode(name: String): JSONArray {
	val client = OkHttpClient()
	val request = Request.Builder()
		.url("http://api.openweathermap.org/geo/1.0/direct?q=$name,RU&limit=2&appid=e7a53fb43aeccf8fff05234eccf7a50c")
		.get()
		.build()

	val jsonData = client.newCall(request).execute().body()!!.string()
	return JSONArray(jsonData)
}
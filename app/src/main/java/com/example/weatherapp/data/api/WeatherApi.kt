package com.example.weatherapp.data.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

fun getWeather(lat: String, lon: String): JSONArray {
	val client = OkHttpClient()
	val request = Request.Builder()
		.url("https://api.openweathermap.org/data/2.5/forecast?" +
				 "lat=$lat&" +
				 "lon=$lon&" +
				 "appid=e7a53fb43aeccf8fff05234eccf7a50c&" +
				 "units=metric")
		.get()
		.build()

	return getResponse(client.newCall(request).execute())
}

private fun getResponse(response: Response): JSONArray {
	val jsonData = response.body()!!.string()
	val jsonObject = JSONObject(jsonData)
	return jsonObject.getJSONArray("list")
}
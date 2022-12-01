package com.example.weatherapp

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class WeatherApi {

	private val key = "e7a53fb43aeccf8fff05234eccf7a50c"

	fun getWeather(): JSONArray {
		val client = OkHttpClient()
		val request = Request.Builder()
			.url("https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=$key")
			.get()
			.build()

		return getResponse(client.newCall(request).execute())
	}

	private fun getResponse(response: Response): JSONArray {
		val jsonData = response.body()!!.string()
		val jsonObject = JSONObject(jsonData)
		return jsonObject.getJSONArray("response")
	}
}
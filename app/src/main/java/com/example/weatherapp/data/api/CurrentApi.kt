package com.example.weatherapp.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request

class CurrentApi {

    fun get(vararg elements: String): JsonObject? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?" +
                    "lat=${elements[0]}&" +
                    "lon=${elements[1]}&" +
                    "appid=${OpenWeatherConfigProvider.key}&" +
                    "units=metric")
            .get()
            .build()

        val jsonData = client.newCall(request).execute().body()!!.string()
        return JsonParser.parseString(jsonData).asJsonObject
    }
}
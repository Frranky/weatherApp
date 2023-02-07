package com.example.weatherapp.data.mapper

import com.example.weatherapp.domain.model.ForecastModel
import com.example.weatherapp.data.model.ResponseModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.reflect.Type

fun toForecastModel(jsonArray: JsonArray): ArrayList<ForecastModel> {
	val result: ArrayList<ForecastModel> = arrayListOf()

	for (i in 0 until jsonArray.size()) {
		val gSon = GsonBuilder().registerTypeAdapter(ForecastModel::class.java, ForecastDeserializer()).create()
		result.add(gSon.fromJson(jsonArray.get(i), ForecastModel::class.java))
	}
	return result
}

fun toForecastModel(text: String): ResponseModel {
	val jsonObject = JsonParser.parseString(text).asJsonObject
	val result: ArrayList<ForecastModel> = arrayListOf()
	val first = jsonObject.get("first").asJsonArray
	val second = jsonObject.get("second").asLong

	for (i in 0 until first.size())
		result.add(Gson().fromJson(first.get(i), ForecastModel::class.java))

	return ResponseModel(result, second)
}

private class ForecastDeserializer : JsonDeserializer<ForecastModel> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ForecastModel {
		json as JsonObject

		val main = json.getAsJsonObject("main")

		val temp = main.get("temp").asString
		val feelsLike = main.get("feels_like").asString
		val weather = json.getAsJsonArray("weather").get(0).asJsonObject.get("description").asString
		val windSpeed = json.getAsJsonObject("wind").get("speed").asString
		val humidity = main.get("humidity").asString
		val date = json.get("dt_txt").asString

		return ForecastModel(temp, feelsLike, weather, windSpeed, humidity, date)
	}
}
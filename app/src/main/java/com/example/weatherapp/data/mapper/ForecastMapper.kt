package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.ForecastModel
import org.json.JSONArray
import org.json.JSONObject

fun toForecastModel(jsonArray: JSONArray): ArrayList<ForecastModel> {
	var result: ArrayList<ForecastModel> = arrayListOf()

	for(i in 0 until jsonArray.length()) {
		val temp 		= jsonArray.getJSONObject(i).getJSONObject("main").getString("temp")
		val feels_like 	= jsonArray.getJSONObject(i).getJSONObject("main").getString("feels_like")
		val weather 	= jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")
		val wind_speed 	= jsonArray.getJSONObject(i).getJSONObject("wind").getString("speed")
		val humidity	= jsonArray.getJSONObject(i).getJSONObject("main").getString("humidity")
		val date 		= jsonArray.getJSONObject(i).getString("dt_txt")
		result.add(ForecastModel(temp, feels_like, weather, wind_speed, humidity, date))
	}
	return result
}

fun toForecastModel(text: String): Pair<ArrayList<ForecastModel>, Long> {
	val jsonObject = JSONObject(text)
	var result: ArrayList<ForecastModel> = arrayListOf()
	val first = jsonObject.getJSONArray("first")
	val second = jsonObject.getString("second").toLong()

	for(i in 0 until first.length()) {
		val temp 		= first.getJSONObject(i).getString("temp")
		val feels_like 	= first.getJSONObject(i).getString("feels_like")
		val weather 	= first.getJSONObject(i).getString("weather")
		val wind_speed 	= first.getJSONObject(i).getString("wind_speed")
		val humidity 	= first.getJSONObject(i).getString("humidity")
		val date 		= first.getJSONObject(i).getString("date")
		result.add(ForecastModel(temp, feels_like, weather, wind_speed, humidity, date))
	}
	return result to second
}
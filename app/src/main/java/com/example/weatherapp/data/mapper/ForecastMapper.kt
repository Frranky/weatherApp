package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.ForecastModel
import org.json.JSONArray

fun toForecastModel(jsonArray: JSONArray): ArrayList<ForecastModel> {
	var result: ArrayList<ForecastModel> = arrayListOf()

	for(i in 0 until jsonArray.length()) {
		val temp 		= jsonArray.getJSONObject(i).getJSONObject("main").getString("temp")
		val feels_like 	= jsonArray.getJSONObject(i).getJSONObject("main").getString("feels_like")
		val weather 	= jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")
		val wind_speed 	= jsonArray.getJSONObject(i).getJSONObject("wind").getString("speed")
		val date 		= jsonArray.getJSONObject(i).getString("dt_txt")
		result.add(ForecastModel(temp, feels_like, weather, wind_speed, date))
	}
	return result
}

fun toForecastModel(text: String): ArrayList<ForecastModel> {
	val jsonArray = JSONArray(text)
	var result: ArrayList<ForecastModel> = arrayListOf()

	for(i in 0 until jsonArray.length()) {
		val temp 		= jsonArray.getJSONObject(i).getString("temp")
		val feels_like 	= jsonArray.getJSONObject(i).getString("feels_like")
		val weather 	= jsonArray.getJSONObject(i).getString("weather")
		val wind_speed 	= jsonArray.getJSONObject(i).getString("wind")
		val date 		= jsonArray.getJSONObject(i).getString("date")
		result.add(ForecastModel(temp, feels_like, weather, wind_speed, date))
	}
	return result
}
package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.GeocodeModel
import org.json.JSONArray
import org.json.JSONObject

fun toGeocodeModel(jsonObject: JSONArray): GeocodeModel {
	val lat 	= jsonObject.getJSONObject(0).getString("lat")
	val lon 	= jsonObject.getJSONObject(0).getString("lon")
	return GeocodeModel(lat, lon)
}
package com.example.weatherapp.data.mapper

import com.example.weatherapp.domain.model.GeocodeModel
import com.google.gson.Gson
import com.google.gson.JsonArray

fun toGeocodeModel(jsonObject: JsonArray): GeocodeModel = Gson().fromJson(jsonObject.get(0), GeocodeModel::class.java)
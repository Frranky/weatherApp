package com.example.weatherapp.data.mapper

import com.example.weatherapp.domain.model.CurrentModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class CurrentMapper {

    operator fun invoke(jsonObject: JsonObject?): CurrentModel? {
        val gSon = GsonBuilder().registerTypeAdapter(CurrentModel::class.java, CurrentDeserializer()).create()
        return gSon.fromJson(jsonObject, CurrentModel::class.java)
    }
}

private class CurrentDeserializer : JsonDeserializer<CurrentModel> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CurrentModel {
        val main = json!!.asJsonObject["main"].asJsonObject
        val weather = json!!.asJsonObject.getAsJsonArray("weather")[0].asJsonObject["description"].asString
        val temp = main.get("temp").asString
        val feels_like = main.get("feels_like").asString
        val humidity = main.get("humidity").asString
        val wind_speed = json!!.asJsonObject["wind"].asJsonObject["speed"].asString

        return CurrentModel(temp, feels_like, weather, wind_speed, humidity)
    }
}

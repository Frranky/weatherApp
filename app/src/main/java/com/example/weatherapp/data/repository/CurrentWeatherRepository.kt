package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.CurrentApi
import com.example.weatherapp.data.api.GeocodeApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.CurrentMapper
import com.example.weatherapp.data.mapper.GeocodeMapper
import com.example.weatherapp.data.model.ResponseModel
import com.example.weatherapp.domain.model.CurrentModel
import com.example.weatherapp.domain.repository.ICurrentRepository

class CurrentWeatherRepository : ICurrentRepository {

    private val currentApi = CurrentApi()
    private val geocodeApi = GeocodeApi()
    private val geocodeMapper = GeocodeMapper()
    private val currentMapper = CurrentMapper()

    override fun currentWeather(name: String): CurrentModel {
        try {
            val geocode = geocodeMapper(geocodeApi.get(name))
            return currentMapper(currentApi.get(geocode.lat, geocode.lon))!!
        }
        catch (e: Exception) {
            return CurrentModel("", "", "", "", "")
        }
    }
}
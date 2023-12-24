package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.model.ResponseModel
import com.example.weatherapp.domain.model.CurrentModel

interface ICurrentRepository {

    fun currentWeather(name: String): CurrentModel
}
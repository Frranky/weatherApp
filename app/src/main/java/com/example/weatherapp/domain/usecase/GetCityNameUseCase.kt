package com.example.weatherapp.domain.usecase

import android.content.Context
import java.io.File
import java.io.FileInputStream

class GetCityNameUseCase {

	operator fun invoke(context: Context): String {
		val path = context.filesDir
		val letDirectory = File(path, "json")
		val file = File(letDirectory, "city.json")

		if (file.exists()) {
			return FileInputStream(file).bufferedReader().use { it.readText() }
		}
		return "Moscow"
	}
}
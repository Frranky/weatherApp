package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.data.api.getWeather
import com.example.weatherapp.data.mapper.toForecastModel
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.util.ISO8601Utils.format
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))

		GlobalScope.launch {
			val data = toForecastModel(getWeather())
			saveData(data)

			val cash = readData()//toForecastModel(readData())
			val currentDate = cash.first
			val data1 = toForecastModel(cash.second)

			runOnUiThread {
				binding.text.text = /*"$cashDate ${currentDate.time}"*/"${data[0].temp} ${data[0].feels_like} && ${data1[0].temp} ${data1[0].feels_like}"
			}
		}
		setContentView(binding.root)
	}

	private fun saveData(data: ArrayList<ForecastModel>) {
		val gsonBuilder = GsonBuilder()
		val gson = gsonBuilder.create()
		val json = gson.toJson(data)
		val path = this.baseContext.filesDir
		val letDirectory = File(path, "json")
		letDirectory.mkdirs()
		val file = File(letDirectory, "data.json")
		file.appendText(json)
	}

	private fun readData(): Pair<Long, String> {
		val path = this.baseContext.filesDir
		val letDirectory = File(path, "json")
		letDirectory.mkdirs()
		val file = File(letDirectory, "data.json")
		return file.lastModified() to FileInputStream(file).bufferedReader().use { it.readText() }
	}
}
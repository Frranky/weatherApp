package com.example.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.repository.LocalDataRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.domain.usecase.GetCityNameUseCase
import com.example.weatherapp.domain.usecase.GetWeatherForecastUseCase
import com.example.weatherapp.presentation.MainActivityViewModel
import com.example.weatherapp.presentation.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityMainBinding
	private val binding get() = _binding
	private var searchFlag = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		supportFragmentManager.beginTransaction().add(R.id.container, PreLoaderFragment()).commit()
		val localDataRepository = LocalDataRepository(this.baseContext)
		val getCityNameUseCase = GetCityNameUseCase(localDataRepository)
		val getDataUseCase = GetWeatherForecastUseCase(localDataRepository)

		val mainActivityViewModel: MainActivityViewModel =
			ViewModelProvider(this, MainActivityViewModelFactory(binding, getCityNameUseCase, getDataUseCase))[MainActivityViewModel::class.java]

		binding.navView.setOnItemSelectedListener {
			when (it.itemId) {
				R.id.temperature -> {
					mainActivityViewModel.setTempChart("Temperature")
					true
				}

				R.id.wind        -> {
					mainActivityViewModel.setTempChart("Wind (m/sec.)")
					true
				}

				R.id.humidity    -> {
					mainActivityViewModel.setTempChart("Humidity (%)")
					true
				}

				else             -> true
			}
		}

		binding.toolbar.information.setOnClickListener {
			val myDialogFragment = AboutDialogFragment()
			val manager = supportFragmentManager
			val transaction: FragmentTransaction = manager.beginTransaction()
			myDialogFragment.show(transaction, "dialog")
		}

		binding.toolbar.explore.setOnClickListener {
			if (!searchFlag) {
				searchFlag = true
				binding.searchBar.visibility = View.VISIBLE
				binding.searchBar.startAnimation(AnimationUtils.loadAnimation(this.baseContext, R.anim.down))
				return@setOnClickListener
			}
			searchFlag = false
			binding.searchBar.visibility = View.INVISIBLE
			binding.searchBar.startAnimation(AnimationUtils.loadAnimation(this.baseContext, R.anim.up))
		}

		binding.searchButton.setOnClickListener {
			mainActivityViewModel.fetchData(binding.searchText.text.toString())
		}
		setContentView(binding.root)
	}
}
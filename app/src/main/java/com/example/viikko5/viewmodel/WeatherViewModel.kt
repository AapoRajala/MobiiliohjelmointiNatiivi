package com.example.viikko5.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikko5.BuildConfig
import com.example.viikko5.data.model.WeatherResponse
import com.example.viikko5.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weatherData: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WeatherViewModel : ViewModel() {
    var city by mutableStateOf("")
        private set

    var uiState by mutableStateOf(WeatherUiState())
        private set

    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    fun updateCity(newCity: String) {
        city = newCity
    }

    fun fetchWeather() {
        if (apiKey.isBlank()) {
            uiState = uiState.copy(errorMessage = "API-avain puuttuu local.properties-tiedostosta!")
            return
        }

        if (city.isBlank()) {
            uiState = uiState.copy(errorMessage = "Syötä kaupungin nimi")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, weatherData = null)
            try {
                val response = RetrofitInstance.api.getWeather(city, apiKey)
                uiState = uiState.copy(weatherData = response, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Virhe haettaessa säätä: ${e.localizedMessage}"
                )
            }
        }
    }
}

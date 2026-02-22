package com.example.weather_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.BuildConfig
import com.example.weather_app.data.model.WeatherEntity
import com.example.weather_app.data.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weatherData: WeatherEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    var city by mutableStateOf("")
        private set

    var uiState by mutableStateOf(WeatherUiState())
        private set

    val weatherHistory: StateFlow<List<WeatherEntity>> = repository.allWeatherHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
                val weather = repository.getWeather(city, apiKey)
                uiState = uiState.copy(weatherData = weather, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Virhe: ${e.localizedMessage}"
                )
            }
        }
    }

    fun deleteCityFromHistory(cityName: String) {
        viewModelScope.launch {
            repository.deleteFromHistory(cityName)
        }
    }
}

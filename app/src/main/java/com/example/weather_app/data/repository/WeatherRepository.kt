package com.example.weather_app.data.repository

import com.example.weather_app.data.local.WeatherDao
import com.example.weather_app.data.model.WeatherEntity
import com.example.weather_app.data.remote.WeatherApi
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) {
    val allWeatherHistory: Flow<List<WeatherEntity>> = weatherDao.getAllWeatherHistory()

    suspend fun getWeather(city: String, apiKey: String): WeatherEntity? {
        val cachedWeather = weatherDao.getWeatherByCity(city)
        val thirtyMinutesInMillis = 30 * 60 * 1000
        val currentTime = System.currentTimeMillis()

        if (cachedWeather != null && (currentTime - cachedWeather.timestamp) < thirtyMinutesInMillis) {
            return cachedWeather
        }

        return try {
            val response = weatherApi.getWeather(city, apiKey)
            val newEntity = WeatherEntity(
                cityName = response.name,
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "Ei kuvausta",
                humidity = response.main.humidity,
                timestamp = currentTime
            )
            weatherDao.insertWeather(newEntity)
            newEntity
        } catch (e: Exception) {
            // Jos verkkokutsu epäonnistuu, palautetaan vanha välimuisti jos se löytyy
            cachedWeather ?: throw e
        }
    }

    suspend fun deleteFromHistory(city: String) {
        weatherDao.deleteWeather(city)
    }
}

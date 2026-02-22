package com.example.weather_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val cityName: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val timestamp: Long // System.currentTimeMillis()
)

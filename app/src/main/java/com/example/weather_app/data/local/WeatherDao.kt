package com.example.weather_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE cityName = :cityName LIMIT 1")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Query("SELECT * FROM weather_cache ORDER BY timestamp DESC")
    fun getAllWeatherHistory(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_cache WHERE cityName = :cityName")
    suspend fun deleteWeather(cityName: String)
}

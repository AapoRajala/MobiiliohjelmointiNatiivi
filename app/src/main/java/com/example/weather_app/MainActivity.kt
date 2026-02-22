package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.data.local.AppDatabase
import com.example.weather_app.data.remote.RetrofitInstance
import com.example.weather_app.data.repository.WeatherRepository
import com.example.weather_app.ui.WeatherScreen
import com.example.weather_app.ui.theme.Weather_appTheme
import com.example.weather_app.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Alustetaan tietokanta ja repository
        val database = AppDatabase.getDatabase(this)
        val repository = WeatherRepository(RetrofitInstance.api, database.weatherDao())
        
        enableEdgeToEdge()
        setContent {
            Weather_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Luodaan ViewModel käyttäen Factorya, jotta saamme repositoryn mukaan
                    val viewModel: WeatherViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return WeatherViewModel(repository) as T
                            }
                        }
                    )
                    
                    WeatherScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

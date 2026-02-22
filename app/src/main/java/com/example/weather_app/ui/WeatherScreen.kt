package com.example.weather_app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather_app.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val uiState = viewModel.uiState
    val history by viewModel.weatherHistory.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = viewModel.city,
            onValueChange = { viewModel.updateCity(it) },
            label = { Text("Syötä kaupunki ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.fetchWeather() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hae sää")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        uiState.weatherData?.let { weather ->
            WeatherResultSection(weather = weather)
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        HorizontalDivider()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Hakuhistoria",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(history) { item ->
                ListItem(
                    headlineContent = { Text(item.cityName) },
                    supportingContent = { 
                        Text("${item.temperature} °C - ${item.description}")
                    },
                    trailingContent = {
                        IconButton(onClick = { viewModel.deleteCityFromHistory(item.cityName) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Poista")
                        }
                    },
                    modifier = Modifier.clickable {
                        viewModel.updateCity(item.cityName)
                        viewModel.fetchWeather()
                    }
                )
            }
        }
    }
}

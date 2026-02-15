package com.example.viikko5.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko5.data.model.WeatherResponse

@Composable
fun WeatherResultSection(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sää: ${weather.name}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "${weather.main.temp} °C",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Kosteus: ${weather.main.humidity}%")
        }
    }
}
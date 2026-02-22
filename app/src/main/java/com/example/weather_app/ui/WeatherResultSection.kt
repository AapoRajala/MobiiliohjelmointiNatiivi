package com.example.weather_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather_app.data.model.WeatherEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherResultSection(weather: WeatherEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sää: ${weather.cityName}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "${weather.temperature} °C",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = weather.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Kosteus: ${weather.humidity}%")
            
            Spacer(modifier = Modifier.height(8.dp))
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val date = sdf.format(Date(weather.timestamp))
            Text(
                text = "Päivitetty: $date",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

package com.aravind.parva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.aravind.parva.data.preferences.UserPreferencesManager
import com.aravind.parva.ui.theme.ParvaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferencesManager = remember { UserPreferencesManager(applicationContext) }
            val isDarkTheme by preferencesManager.darkThemeFlow.collectAsState(initial = false)
            val useDynamicColors by preferencesManager.useDynamicColorsFlow.collectAsState(initial = false)
            
            ParvaTheme(
                darkTheme = isDarkTheme,
                dynamicColor = useDynamicColors
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParvaApp(preferencesManager = preferencesManager)
                }
            }
        }
    }
}


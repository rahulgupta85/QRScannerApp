package com.example.appdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Added
import androidx.compose.runtime.collectAsState // Added
import androidx.compose.runtime.getValue // Added
import com.example.appdev.ui.screens.SettingsViewModel // Added
import com.example.appdev.ui.navigation.NavGraph
import com.example.appdev.ui.theme.AllInOneQRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            AllInOneQRTheme(
                darkTheme = isDarkMode
            ) {
                NavGraph()
            }
        }
    }
}
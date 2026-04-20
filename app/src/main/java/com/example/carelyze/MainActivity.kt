package com.example.carelyze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.carelyze.di.App
import com.example.carelyze.navigation.NavGraph
import com.example.carelyze.navigation.Screen
import com.example.carelyze.ui.theme.CarelyzeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val app = application as App
        val preferencesManager = app.component.preferencesManager()
        val startDestination = if (!preferencesManager.isOnboardingShown()) {
            Screen.Onboarding.route
        } else if (preferencesManager.getAccessToken().isNullOrBlank()) {
            Screen.Auth.route
        } else {
            Screen.Scanner.route
        }
        
        setContent {
            CarelyzeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        preferencesManager = preferencesManager,
                        viewModelFactory = app.component.viewModelFactory(),
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
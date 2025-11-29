package com.aravind.parva

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aravind.parva.ui.screens.HomeScreen
import com.aravind.parva.ui.screens.ParvaDetailScreen

@Composable
fun ParvaApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onParvaClick = { parvaId ->
                    navController.navigate("parva/$parvaId")
                }
            )
        }
        composable("parva/{parvaId}") { backStackEntry ->
            val parvaId = backStackEntry.arguments?.getString("parvaId")
            ParvaDetailScreen(
                parvaId = parvaId ?: "",
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}


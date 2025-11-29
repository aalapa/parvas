package com.aravind.parva

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aravind.parva.ui.screens.*

@Composable
fun ParvaApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onMahaParvaClick = { mahaParvaId ->
                    navController.navigate("mahaparva/$mahaParvaId")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}",
            arguments = listOf(navArgument("mahaParvaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            MahaParvaDetailScreen(
                mahaParvaId = mahaParvaId,
                onBackClick = { navController.popBackStack() },
                onParvaClick = { parvaIndex ->
                    navController.navigate("mahaparva/$mahaParvaId/parva/$parvaIndex/list")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}/parva/{parvaIndex}/{viewMode}",
            arguments = listOf(
                navArgument("mahaParvaId") { type = NavType.StringType },
                navArgument("parvaIndex") { type = NavType.IntType },
                navArgument("viewMode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            val parvaIndex = backStackEntry.arguments?.getInt("parvaIndex") ?: 0
            val viewMode = backStackEntry.arguments?.getString("viewMode") ?: "list"
            ParvaDetailScreen(
                mahaParvaId = mahaParvaId,
                parvaIndex = parvaIndex,
                viewMode = viewMode,
                onBackClick = { navController.popBackStack() },
                onSaptahaClick = { saptahaIndex ->
                    navController.navigate("mahaparva/$mahaParvaId/parva/$parvaIndex/saptaha/$saptahaIndex")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}/parva/{parvaIndex}/saptaha/{saptahaIndex}",
            arguments = listOf(
                navArgument("mahaParvaId") { type = NavType.StringType },
                navArgument("parvaIndex") { type = NavType.IntType },
                navArgument("saptahaIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            val parvaIndex = backStackEntry.arguments?.getInt("parvaIndex") ?: 0
            val saptahaIndex = backStackEntry.arguments?.getInt("saptahaIndex") ?: 0
            SaptahaDetailScreen(
                mahaParvaId = mahaParvaId,
                parvaIndex = parvaIndex,
                saptahaIndex = saptahaIndex,
                onBackClick = { navController.popBackStack() },
                onDinaClick = { dinaIndex ->
                    navController.navigate("mahaparva/$mahaParvaId/parva/$parvaIndex/saptaha/$saptahaIndex/dina/$dinaIndex")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}/parva/{parvaIndex}/saptaha/{saptahaIndex}/dina/{dinaIndex}",
            arguments = listOf(
                navArgument("mahaParvaId") { type = NavType.StringType },
                navArgument("parvaIndex") { type = NavType.IntType },
                navArgument("saptahaIndex") { type = NavType.IntType },
                navArgument("dinaIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            val parvaIndex = backStackEntry.arguments?.getInt("parvaIndex") ?: 0
            val saptahaIndex = backStackEntry.arguments?.getInt("saptahaIndex") ?: 0
            val dinaIndex = backStackEntry.arguments?.getInt("dinaIndex") ?: 0
            DinaDetailScreen(
                mahaParvaId = mahaParvaId,
                parvaIndex = parvaIndex,
                saptahaIndex = saptahaIndex,
                dinaIndex = dinaIndex,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onExportJournalClick = { /* Handle journal export */ },
                onExportDataClick = { /* Handle data export */ },
                onImportDataClick = { /* Handle data import */ }
            )
        }
    }
}


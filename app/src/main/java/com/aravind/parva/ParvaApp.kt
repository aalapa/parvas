package com.aravind.parva

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aravind.parva.data.local.ParvaDatabase
import com.aravind.parva.data.preferences.UserPreferencesManager
import com.aravind.parva.data.repository.MahaParvaRepository
import com.aravind.parva.ui.screens.*
import com.aravind.parva.viewmodel.HomeViewModel
import com.aravind.parva.viewmodel.HomeViewModelFactory
import com.aravind.parva.viewmodel.MahaParvaViewModel
import com.aravind.parva.viewmodel.MahaParvaViewModelFactory

@Composable
fun ParvaApp(preferencesManager: UserPreferencesManager) {
    val context = LocalContext.current
    val navController = rememberNavController()
    
    // Initialize database and repository
    val database = remember { ParvaDatabase.getDatabase(context) }
    val repository = remember { MahaParvaRepository(database.mahaParvaDao()) }
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
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
            val viewModel: MahaParvaViewModel = viewModel(
                key = mahaParvaId,
                factory = MahaParvaViewModelFactory(repository, mahaParvaId)
            )
            MahaParvaDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onParvaClick = { parvaIndex, yojanaMode ->
                    navController.navigate("mahaparva/$mahaParvaId/parva/$parvaIndex/list/$yojanaMode")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}/parva/{parvaIndex}/{viewMode}/{yojanaMode}",
            arguments = listOf(
                navArgument("mahaParvaId") { type = NavType.StringType },
                navArgument("parvaIndex") { type = NavType.IntType },
                navArgument("viewMode") { type = NavType.StringType },
                navArgument("yojanaMode") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            val parvaIndex = backStackEntry.arguments?.getInt("parvaIndex") ?: 0
            val viewMode = backStackEntry.arguments?.getString("viewMode") ?: "list"
            val yojanaMode = backStackEntry.arguments?.getBoolean("yojanaMode") ?: false
            val viewModel: MahaParvaViewModel = viewModel(
                key = mahaParvaId,
                factory = MahaParvaViewModelFactory(repository, mahaParvaId)
            )
            ParvaDetailScreen(
                viewModel = viewModel,
                parvaIndex = parvaIndex,
                viewMode = viewMode,
                yojanaMode = yojanaMode,
                onBackClick = { navController.popBackStack() },
                onSaptahaClick = { saptahaIndex, yojanaMode ->
                    navController.navigate("mahaparva/$mahaParvaId/parva/$parvaIndex/saptaha/$saptahaIndex/$yojanaMode")
                }
            )
        }
        
        composable(
            "mahaparva/{mahaParvaId}/parva/{parvaIndex}/saptaha/{saptahaIndex}/{yojanaMode}",
            arguments = listOf(
                navArgument("mahaParvaId") { type = NavType.StringType },
                navArgument("parvaIndex") { type = NavType.IntType },
                navArgument("saptahaIndex") { type = NavType.IntType },
                navArgument("yojanaMode") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId") ?: ""
            val parvaIndex = backStackEntry.arguments?.getInt("parvaIndex") ?: 0
            val saptahaIndex = backStackEntry.arguments?.getInt("saptahaIndex") ?: 0
            val yojanaMode = backStackEntry.arguments?.getBoolean("yojanaMode") ?: false
            val viewModel: MahaParvaViewModel = viewModel(
                key = mahaParvaId,
                factory = MahaParvaViewModelFactory(repository, mahaParvaId)
            )
            SaptahaDetailScreen(
                viewModel = viewModel,
                parvaIndex = parvaIndex,
                saptahaIndex = saptahaIndex,
                yojanaMode = yojanaMode,
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
            val viewModel: MahaParvaViewModel = viewModel(
                key = mahaParvaId,
                factory = MahaParvaViewModelFactory(repository, mahaParvaId)
            )
            DinaDetailScreen(
                viewModel = viewModel,
                parvaIndex = parvaIndex,
                saptahaIndex = saptahaIndex,
                dinaIndex = dinaIndex,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                preferencesManager = preferencesManager,
                repository = repository,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}


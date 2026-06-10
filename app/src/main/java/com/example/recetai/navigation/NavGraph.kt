package com.example.recetai.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recetai.ui.auth.*
import com.example.recetai.ui.home.*
import com.example.recetai.ui.inventory.InventoryScreen
import com.example.recetai.ui.recipes.*
import com.example.recetai.ui.support.*
import com.example.recetai.ui.profile.ProfileScreen
import com.example.recetai.ui.scan.ScanScreen
import com.google.firebase.auth.FirebaseAuth

object Route {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val RECIPE_RESULTS = "recipe_results"
    const val INVENTORY = "inventory"
    const val PROFILE = "profile"
    const val HISTORY = "history"
    const val CONTACT = "contact"
    const val SCAN = "scan"
    const val TERMS = "terms"
    const val INGREDIENT_REVIEW = "ingredient_review"
    const val FORGOTPASSWORD = "forgot_password"
    const val FAVORITES = "favorites"
    const val HELP = "help"
    const val STATS = "stats"
    const val RECIPE_DETAIL = "recipe_detail"
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel,
    darkThemeEnabled: Boolean,
    onThemeChanged: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChanged: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Route.SPLASH,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.SPLASH) {
            SplashScreen(
                onNavigateHome = { navController.navigate(Route.HOME) { popUpTo(Route.SPLASH) { inclusive = true } } },
                onNavigateLogin = { navController.navigate(Route.LOGIN) { popUpTo(Route.SPLASH) { inclusive = true } } }
            )
        }

        composable(Route.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToScan = { navController.navigate(Route.SCAN) },
                onNavigateToProfile = { navController.navigate(Route.PROFILE) },
                onNavigateToDetail = { navController.navigate(Route.RECIPE_DETAIL) }
            )
        }

        composable(Route.INVENTORY) { InventoryScreen() }

        composable(Route.RECIPE_RESULTS) {
            val recipes by homeViewModel.searchResults.collectAsState()
            RecipeResultScreen(
                recipes = recipes,
                onBackHome = { navController.popBackStack() },
                onRecipeClick = { receta ->
                    homeViewModel.selectRecipe(receta)
                    navController.navigate(Route.RECIPE_DETAIL)
                }
            )
        }

        composable(Route.RECIPE_DETAIL) {
            val recipeOpt by homeViewModel.selectedRecipe.collectAsState()
            val favoriteIds by homeViewModel.favoriteIds.collectAsState()

            recipeOpt?.let { receta ->
                RecipeDetailScreen(
                    recipe = receta,
                    isFavorite = favoriteIds.contains(receta.id),
                    onToggleFavorite = { homeViewModel.toggleFavorite(receta) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Route.SCAN) {
            ScanScreen(onSearchRecipes = { ingredients ->
                homeViewModel.setIngredients(ingredients)
                navController.navigate(Route.INGREDIENT_REVIEW)
            })
        }

        composable(Route.INGREDIENT_REVIEW) {
            val ingredients by homeViewModel.lastScannedIngredients.collectAsState()
            IngredientReviewScreen(
                initialIngredients = ingredients,
                onConfirm = { list ->
                    homeViewModel.guardarIngredientesAlInventario(list)
                    homeViewModel.guardarEnHistorial(list)
                    homeViewModel.findRecipesOptimized(list)
                    navController.navigate(Route.RECIPE_RESULTS)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.FAVORITES) {
            // 1. Recolectamos los estados necesarios del ViewModel
            val favoriteIds by homeViewModel.favoriteIds.collectAsState()
            val allRecipes by homeViewModel.recommendedRecipes.collectAsState()

            // 2. Filtramos las recetas que son favoritas directamente aquí
            val favoriteRecipes = allRecipes.filter { favoriteIds.contains(it.id) }

            // 3. Pasamos solo la lista filtrada y las funciones necesarias
                FavoritesScreen(
                    // Aquí pasamos el viewModel tal como lo espera tu pantalla actual
                    viewModel = homeViewModel,
                    onNavigateToDetail = { receta ->
                        homeViewModel.selectRecipe(receta)
                        navController.navigate(Route.RECIPE_DETAIL)
                    }
                )
        }

        composable(Route.PROFILE) {
            // Asegúrate de que estas variables coincidan con las de tu HomeViewModel
            val favCount by homeViewModel.favoriteCount.collectAsState()
            val hisCount by homeViewModel.historyCount.collectAsState()

            ProfileScreen(
                isDarkMode = darkThemeEnabled,
                onThemeChanged = onThemeChanged,
                currentLanguage = currentLanguage,
                onLanguageChanged = onLanguageChanged,
                onNavigateToLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Route.LOGIN) { popUpTo(0) { inclusive = true } }
                },
                onNavigateToTerms = { navController.navigate(Route.TERMS) },
                onNavigateToContact = { navController.navigate(Route.CONTACT) },
                onNavigateToHistory = { navController.navigate(Route.HISTORY) },
                onNavigateToHelp = { navController.navigate(Route.HELP) },
                onNavigateToStats = { navController.navigate(Route.STATS) },
                favoriteCount = favCount,
                historyCount = hisCount
            )
        }

        composable(Route.HISTORY) { HistoryScreen(onBack = { navController.popBackStack() }) }
        composable(Route.TERMS) { TerminosScreen(onBack = { navController.popBackStack() }) }
        composable(Route.CONTACT) { ContactScreen(onBack = { navController.popBackStack() }) }
        composable(Route.HELP) { HelpScreen(onBack = { navController.popBackStack() }) }
        composable(Route.STATS) {
            val favCount by homeViewModel.favoriteCount.collectAsState()
            val hisCount by homeViewModel.historyCount.collectAsState()
            val invCount by homeViewModel.inventoryCount.collectAsState()
            StatsScreen(favCount, hisCount, invCount, onBack = { navController.popBackStack() })
        }

        composable(Route.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Route.HOME) { popUpTo(Route.LOGIN) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Route.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Route.FORGOTPASSWORD) }
            )
        }

        composable(Route.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Route.LOGIN) },
                onNavigateToLogin = { navController.navigate(Route.LOGIN) }
            )
        }

        composable(Route.FORGOTPASSWORD) { ForgotPasswordScreen(onBack = { navController.popBackStack() }) }
    }
}
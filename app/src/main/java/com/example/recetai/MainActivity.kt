package com.example.recetai

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recetai.ui.theme.RecetAITheme
import java.util.Locale

object Route {

    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val RECIPE_RESULTS = "recipe_results"
    const val PROFILE = "profile"
    const val CONTACT = "contact"
    const val SCAN = "scan"
    const val TERMS = "terms"

    const val INGREDIENT_REVIEW = "ingredient_review"
    const val FORGOTPASSWORD = "forgot_password"
    const val SETTINGS = "settings"
    const val HELP = "help"
    const val STATS = "stats"
    const val RATING = "rating"
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref =
            getSharedPreferences(
                "RecetAI_Prefs",
                Context.MODE_PRIVATE
            )

        setContent {

            var darkThemeEnabled by rememberSaveable {
                mutableStateOf(
                    sharedPref.getBoolean(
                        "isDarkMode",
                        true
                    )
                )
            }

            var currentLanguage by rememberSaveable {
                mutableStateOf(
                    sharedPref.getString(
                        "language",
                        "es"
                    ) ?: "es"
                )
            }

            val locale = Locale(currentLanguage)

            Locale.setDefault(locale)

            val homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

            var scannedIngredients by remember {

                mutableStateOf<List<String>>(
                    emptyList()
                )
            }

            val favoriteCount by
            homeViewModel.favoriteCount.collectAsState()

            val historyCount by
            homeViewModel.historyCount.collectAsState()

            val configuration =
                LocalConfiguration.current

            configuration.setLocale(locale)

            val resources =
                LocalContext.current.resources

            resources.updateConfiguration(
                configuration,
                resources.displayMetrics
            )

            RecetAITheme(
                darkTheme = darkThemeEnabled
            ) {

                val navController =
                    rememberNavController()

                val navBackStackEntry by
                navController.currentBackStackEntryAsState()

                val currentDestination =
                    navBackStackEntry
                        ?.destination
                        ?.route

                Scaffold(

                    bottomBar = {

                        if (
                            currentDestination != Route.LOGIN &&
                            currentDestination != Route.REGISTER &&
                            currentDestination != Route.SPLASH
                        ) {

                            NavigationBar {

                                NavigationBarItem(
                                    selected =
                                        currentDestination == Route.HOME,
                                    onClick = {
                                        navController.navigate(
                                            Route.HOME
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Home,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text("Inicio")
                                    }
                                )

                                NavigationBarItem(
                                    selected =
                                        currentDestination == Route.SCAN,
                                    onClick = {
                                        navController.navigate(
                                            Route.SCAN
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text("Escanear")
                                    }
                                )

                                NavigationBarItem(
                                    selected =
                                        currentDestination == Route.PROFILE,
                                    onClick = {
                                        navController.navigate(
                                            Route.PROFILE
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text("Perfil")
                                    }
                                )
                            }
                        }
                    }

                ) { innerPadding -> NavHost(
                        navController = navController,
                        startDestination = Route.SPLASH,
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    ) {

                        // SPLASH
                        composable(Route.SPLASH) {

                            SplashScreen(

                                onNavigateHome = {

                                    navController.navigate(
                                        Route.HOME
                                    ) {

                                        popUpTo(
                                            Route.SPLASH
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                },

                                onNavigateLogin = {

                                    navController.navigate(
                                        Route.LOGIN
                                    ) {

                                        popUpTo(
                                            Route.SPLASH
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        // HOME
                        composable(Route.HOME) {

                            HomeScreen(
                                onNavigateToScan = {
                                    navController.navigate(
                                        Route.SCAN
                                    )
                                },
                                onNavigateToProfile = {
                                    navController.navigate(
                                        Route.PROFILE
                                    )
                                }
                            )
                        }

                    // RESULTS
                    composable(
                        Route.RECIPE_RESULTS
                    ) {

                        RecipeResultScreen(

                            recipes =
                                homeViewModel
                                    .searchResults
                                    .collectAsState()
                                    .value,

                            onFavoriteClick = { recipe ->

                                homeViewModel.addFavorite(
                                    recipe
                                )
                            },

                            onBackHome = {

                                navController.navigate(
                                    Route.HOME
                                ) {

                                    popUpTo(
                                        Route.HOME
                                    )
                                }
                            }
                        )
                    }

                        // SCAN
                    composable(Route.SCAN) {

                        ScanScreen(

                            onSearchRecipes = { ingredients ->

                                scannedIngredients =
                                    ingredients

                                navController.navigate(
                                    Route.INGREDIENT_REVIEW
                                )
                            }
                        )
                    }

                        // PROFILE
                        composable(Route.PROFILE) {

                            ProfileScreen(

                                isDarkMode = darkThemeEnabled,

                                onThemeChanged = { nuevo ->

                                    darkThemeEnabled =
                                        nuevo

                                    sharedPref.edit()
                                        .putBoolean(
                                            "isDarkMode",
                                            nuevo
                                        )
                                        .apply()
                                },

                                currentLanguage =
                                    currentLanguage,

                                onLanguageChanged = { lang ->

                                    currentLanguage =
                                        lang

                                    sharedPref.edit()
                                        .putString(
                                            "language",
                                            lang
                                        )
                                        .apply()

                                    recreate()
                                },

                                onNavigateToLogin = {

                                    navController.navigate(
                                        Route.LOGIN
                                    ) {

                                        popUpTo(Route.HOME) {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                },

                                onNavigateToRegister = {
                                    navController.navigate(
                                        Route.REGISTER
                                    )
                                },

                                onNavigateToTerms = {
                                    navController.navigate(
                                        Route.TERMS
                                    )
                                },

                                onNavigateToContact = {
                                    navController.navigate(
                                        Route.CONTACT
                                    )
                                },

                                favoriteCount = favoriteCount,

                                historyCount = historyCount
                            )
                        }

                        // TERMS
                        composable(Route.TERMS) {

                            ContratoConElDiablo(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // CONTACT
                        composable(Route.CONTACT) {

                            ContactScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // LOGIN
                        composable(Route.LOGIN) {

                            LoginScreen(

                                onLoginSuccess = {

                                    android.util.Log.d(
                                        "RECETAI",
                                        "LOGIN EXITOSO"
                                    )

                                    navController.navigate(
                                        Route.HOME
                                    ) {

                                        popUpTo(
                                            Route.LOGIN
                                        ) {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                },

                                onNavigateToRegister = {

                                    navController.navigate(
                                        Route.REGISTER
                                    )
                                },

                                onNavigateToForgotPassword = {

                                    navController.navigate(
                                        Route.FORGOTPASSWORD
                                    )
                                }
                            )
                        }

                        //INGREDIENT_REVIEW

                        composable(
                            Route.INGREDIENT_REVIEW
                        ) {

                            IngredientReviewScreen(

                                initialIngredients =
                                    scannedIngredients,

                                onConfirm = { ingredients ->

                                    homeViewModel
                                        .findRecipesByIngredients(
                                            ingredients
                                        )

                                    navController.navigate(
                                        Route.RECIPE_RESULTS
                                    )
                                }
                            )
                        }

                        // LOGIN
                        composable(
                            Route.FORGOTPASSWORD
                        ) {

                            ForgotPasswordScreen(

                                onBack = {

                                    navController.popBackStack()
                                }
                            )
                        }

                        // REGISTER
                        composable(Route.REGISTER) {

                            RegisterScreen(

                                onRegisterSuccess = {

                                    navController.navigate(
                                        Route.LOGIN
                                    )
                                },

                                onNavigateToLogin = {

                                    navController.navigate(
                                        Route.LOGIN
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
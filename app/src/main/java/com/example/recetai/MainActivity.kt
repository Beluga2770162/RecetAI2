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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite

object AppScreen {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val SCAN = "scan"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val TERMS = "terms"
    const val CONTACT = "contact"

    const val FAVORITES = "favorites"
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
                            currentDestination != AppScreen.LOGIN &&
                            currentDestination != AppScreen.REGISTER &&
                            currentDestination != AppScreen.SPLASH
                        ) {

                            NavigationBar {

                                NavigationBarItem(
                                    selected =
                                        currentDestination == AppScreen.HOME,
                                    onClick = {
                                        navController.navigate(
                                            AppScreen.HOME
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
                                        currentDestination == AppScreen.SCAN,
                                    onClick = {
                                        navController.navigate(
                                            AppScreen.SCAN
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
                                        currentDestination == AppScreen.PROFILE,
                                    onClick = {
                                        navController.navigate(
                                            AppScreen.PROFILE
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

                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = AppScreen.SPLASH,
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    ) {

                        // SPLASH
                        composable(AppScreen.SPLASH) {

                            SplashScreen(

                                onNavigateHome = {

                                    navController.navigate(
                                        AppScreen.HOME
                                    ) {

                                        popUpTo(
                                            AppScreen.SPLASH
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                },

                                onNavigateLogin = {

                                    navController.navigate(
                                        AppScreen.LOGIN
                                    ) {

                                        popUpTo(
                                            AppScreen.SPLASH
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        // HOME
                        composable(AppScreen.HOME) {

                            HomeScreen(
                                onNavigateToScan = {
                                    navController.navigate(
                                        AppScreen.SCAN
                                    )
                                },
                                onNavigateToProfile = {
                                    navController.navigate(
                                        AppScreen.PROFILE
                                    )
                                }
                            )
                        }

                        // SCAN
                        composable(AppScreen.SCAN) {
                            ScanScreen()
                        }

                        // PROFILE
                        composable(AppScreen.PROFILE) {

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
                                        AppScreen.LOGIN
                                    )
                                },

                                onNavigateToRegister = {
                                    navController.navigate(
                                        AppScreen.REGISTER
                                    )
                                },

                                onNavigateToTerms = {
                                    navController.navigate(
                                        AppScreen.TERMS
                                    )
                                },

                                onNavigateToContact = {
                                    navController.navigate(
                                        AppScreen.CONTACT
                                    )
                                }
                            )
                        }

                        // TERMS
                        composable(AppScreen.TERMS) {

                            ContratoConElDiablo(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // CONTACT
                        composable(AppScreen.CONTACT) {

                            ContactScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // LOGIN
                        composable(AppScreen.LOGIN) {

                            LoginScreen(

                                onLoginSuccess = {

                                    navController.navigate(
                                        AppScreen.HOME
                                    ) {

                                        popUpTo(
                                            AppScreen.LOGIN
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                },

                                onNavigateToRegister = {

                                    navController.navigate(
                                        AppScreen.REGISTER
                                    )
                                }
                            )
                        }

                        // REGISTER
                        composable(AppScreen.REGISTER) {

                            RegisterScreen(

                                onRegisterSuccess = {

                                    navController.navigate(
                                        AppScreen.LOGIN
                                    )
                                },

                                onNavigateToLogin = {

                                    navController.navigate(
                                        AppScreen.LOGIN
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
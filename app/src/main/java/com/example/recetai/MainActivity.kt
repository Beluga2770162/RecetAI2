package com.example.recetai

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recetai.ui.theme.RecetAITheme
import java.util.Locale

// Diccionario de rutas para la navegación
object AppScreen {
    const val HOME = "home"
    const val SCAN = "scan"
    const val REVIEW = "review"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val TERMS = "terms"
    const val CONTACT = "contact"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos las preferencias compartidas para persistencia
        val sharedPref = getSharedPreferences("RecetAI_Prefs", Context.MODE_PRIVATE)

        setContent {
            // ESTADOS PERSISTENTES (Sobreviven a la navegación y rotación)
            var darkThemeEnabled by rememberSaveable {
                mutableStateOf(sharedPref.getBoolean("isDarkMode", true))
            }
            var currentLanguage by rememberSaveable {
                mutableStateOf(sharedPref.getString("language", "es") ?: "es")
            }

            // APLICACIÓN DINÁMICA DEL IDIOMA
            val locale = Locale(currentLanguage)
            Locale.setDefault(locale)
            val configuration = LocalConfiguration.current
            configuration.setLocale(locale)
            val resources = LocalContext.current.resources
            resources.updateConfiguration(configuration, resources.displayMetrics)

            // APLICACIÓN DEL TEMA
            RecetAITheme(darkTheme = darkThemeEnabled) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppScreen.HOME
                ) {
                    // PANTALLA: HOME
                    composable(AppScreen.HOME) {
                        HomeScreen(
                            onNavigateToScan = { navController.navigate(AppScreen.SCAN) },
                            onNavigateToProfile = { navController.navigate(AppScreen.PROFILE) }
                        )
                    }

                    // PANTALLA: PERFIL (Donde ocurre la magia del cambio)
                    composable(AppScreen.PROFILE) {
                        ProfileScreen(
                            isDarkMode = darkThemeEnabled,
                            onThemeChanged = { nuevoValor ->
                                // Actualizamos estado y guardamos en preferencias
                                darkThemeEnabled = nuevoValor
                                sharedPref.edit().putBoolean("isDarkMode", nuevoValor).apply()
                            },
                            currentLanguage = currentLanguage,
                            onLanguageChanged = { nuevoIdioma ->
                                // Actualizamos estado y guardamos en preferencias
                                currentLanguage = nuevoIdioma
                                sharedPref.edit().putString("language", nuevoIdioma).apply()
                            },
                            onNavigateToLogin = { navController.navigate(AppScreen.LOGIN) },
                            onNavigateToRegister = { navController.navigate(AppScreen.REGISTER) },
                            onNavigateToTerms = { navController.navigate(AppScreen.TERMS) },
                            onNavigateToContact = { navController.navigate(AppScreen.CONTACT) }
                        )
                    }

                    // PANTALLA: LOGIN
                    composable(AppScreen.LOGIN) {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(AppScreen.HOME) {
                                    popUpTo(AppScreen.LOGIN) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate(AppScreen.REGISTER) }
                        )
                    }

                    // PANTALLA: REGISTRO
                    composable(AppScreen.REGISTER) {
                        RegisterScreen(
                            onRegisterSuccess = {
                                navController.navigate(AppScreen.HOME) {
                                    popUpTo(AppScreen.LOGIN) { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { navController.navigate(AppScreen.LOGIN) }
                        )
                    }

                    // PANTALLAS RESTANTES
                    composable(AppScreen.TERMS) {
                        TermsScreen(onBack = { navController.popBackStack() })
                    }
                    composable(AppScreen.CONTACT) {
                        ContactScreen(onBack = { navController.popBackStack() })
                    }

                    // Asegúrate de incluir ScanScreen e IngredientReviewScreen si ya las tienes
                }
            }
        }
    }
}








































































// Este trabajo fue realizado gracias a cantidades insalubres de whiskey barato $120 por botella, google Gemini, al coraje que tome desde el martes cuando vi que mi ex agarro nuevo wey despues de mi accidente, Godzilla, Super Sentai y el tokosatsu en general
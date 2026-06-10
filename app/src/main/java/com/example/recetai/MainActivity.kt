package com.example.recetai

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recetai.navigation.Route
import com.example.recetai.navigation.SetupNavGraph
import com.example.recetai.ui.home.HomeViewModel
import com.example.recetai.ui.theme.RecetAITheme
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("RecetAI_Prefs", Context.MODE_PRIVATE)

        // Aplicar idioma guardado antes de que cargue la UI
        val savedLanguage = sharedPref.getString("language", "es") ?: "es"
        setAppLanguage(this, savedLanguage)

        setContent {
            var darkThemeEnabled by rememberSaveable {
                mutableStateOf(sharedPref.getBoolean("isDarkMode", true))
            }
            var currentLanguage by rememberSaveable {
                mutableStateOf(savedLanguage)
            }

            val homeViewModel: HomeViewModel = viewModel()

            // Carga inicial de datos desde el archivo JSON local
            LaunchedEffect(Unit) {
                try {
                    val jsonString = application.resources.openRawResource(R.raw.recetas)
                        .bufferedReader().use { it.readText() }
                    homeViewModel.inicializarBaseDeDatos(jsonString)
                } catch (e: Exception) {
                    // Manejo silencioso o log de error si no existe el archivo
                }
            }

            RecetAITheme(darkTheme = darkThemeEnabled) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        val hideBottomBar = currentRoute in listOf(Route.LOGIN, Route.REGISTER, Route.SPLASH)
                        if (!hideBottomBar) {
                            NavigationBar {
                                val items = listOf(
                                    Triple(R.string.home, Icons.Default.Home, Route.HOME),
                                    Triple(R.string.inventory, Icons.Default.Inventory, Route.INVENTORY),
                                    Triple(R.string.scan, Icons.Default.Add, Route.SCAN),
                                    Triple(R.string.favorites, Icons.Default.Favorite, Route.FAVORITES),
                                    Triple(R.string.profile, Icons.Default.Person, Route.PROFILE)
                                )

                                items.forEach { (labelRes, icon, route) ->
                                    NavigationBarItem(
                                        selected = currentRoute == route,
                                        onClick = {
                                            navController.navigate(route) {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(icon, contentDescription = stringResource(id = labelRes)) },
                                        label = {
                                            if (currentRoute == route) {
                                                Text(stringResource(id = labelRes), style = MaterialTheme.typography.labelSmall)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    SetupNavGraph(
                        navController = navController,
                        innerPadding = innerPadding,
                        homeViewModel = homeViewModel,
                        darkThemeEnabled = darkThemeEnabled,
                        onThemeChanged = {
                            darkThemeEnabled = it
                            sharedPref.edit().putBoolean("isDarkMode", it).apply()
                        },
                        currentLanguage = currentLanguage,
                        onLanguageChanged = { code ->
                            currentLanguage = code
                            sharedPref.edit().putString("language", code).apply()
                            setAppLanguage(this@MainActivity, code)
                            recreate() // Reinicia la actividad para aplicar el cambio de idioma
                        }
                    )
                }
            }
        }
    }

    private fun setAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
package com.example.recetai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContratoConElDiablo(onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Términos que no vas a leer") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Bienvenido a la Ilusión de la Privacidad",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                SeccionSarcasmo(
                    titulo = "1. Dale a aceptar de una",
                    descripcion = "Al picarle aquí, básicamente nos das las llaves de tu casa. Vamos a leer tus mensajes, ver tus fotos borrosas y saber a qué hora te duermes. Si creías que esta app era gratis porque somos buena onda, felicidades: tú eres el producto que vamos a venderle a un bróker de datos en el extranjero."
                )

                SeccionSarcasmo(
                    titulo = "2. 'Permisos' ",
                    descripcion = "¿Por qué una app de recetas quiere saber tu ubicación exacta y ver tus contactos? No preguntes, solo dale a 'Permitir'. Si Google ya sabe hasta cuántas veces parpadeas al día, ¿qué más da que nosotros también lo sepamos? Al final del día, tu teléfono te está escuchando de todos modos."
                )

                SeccionSarcasmo(
                    titulo = "3. Compartir es vivir (nuestro negocio)",
                    descripcion = "Tus datos van a viajar por medio mundo. Se los vamos a pasar a 'socios comerciales' (o sea, gente que te va a llenar de anuncios de sartenes y dietas milagrosas). No es personal, es capitalismo digital. Si no te gusta, pues apaga el Wi-Fi y vete a vivir al cerro."
                )

                SeccionSarcasmo(
                    titulo = "4. Podemos cambiar las reglas cuando queramos",
                    descripcion = "Si mañana decidimos que ahora la app es obligatoria para graduarte o que nos debes un café cada vez que la abras, así será. Seguir usando la app significa que estás de acuerdo con cualquier locura que se nos ocurra poner aquí el próximo mes."
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Actualizado: hace 2 min (No importa cuanod lo leas).",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Si llegaste hasta aquí leyendo, neta no tienes nada mejor que hacer.",
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun SeccionSarcasmo(titulo: String, descripcion: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = descripcion,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
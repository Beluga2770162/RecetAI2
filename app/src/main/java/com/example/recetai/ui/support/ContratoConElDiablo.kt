package com.example.recetai.ui.support

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recetai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.terms_conditions_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.terms_of_use_heading),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            TerminoItem(
                titulo = stringResource(id = R.string.terms_item_1_title),
                descripcion = stringResource(id = R.string.terms_item_1_desc)
            )
            TerminoItem(
                titulo = stringResource(id = R.string.terms_item_2_title),
                descripcion = stringResource(id = R.string.terms_item_2_desc)
            )
            TerminoItem(
                titulo = stringResource(id = R.string.terms_item_3_title),
                descripcion = stringResource(id = R.string.terms_item_3_desc)
            )
            TerminoItem(
                titulo = stringResource(id = R.string.terms_item_4_title),
                descripcion = stringResource(id = R.string.terms_item_4_desc)
            )
            TerminoItem(
                titulo = stringResource(id = R.string.terms_item_5_title),
                descripcion = stringResource(id = R.string.terms_item_5_desc)
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Inyección dinámica de la versión en el string de formato estructurado
            Text(
                text = stringResource(id = R.string.terms_version_format, "1.0.0"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TerminoItem(titulo: String, descripcion: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
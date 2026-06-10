package com.example.recetai.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recetai.R
import com.google.firebase.auth.FirebaseAuth
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChanged: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToStats: () -> Unit,
    favoriteCount: Int,
    historyCount: Int,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CABECERA ---
        if (user?.photoUrl != null) {
            AsyncImage(model = user.photoUrl, contentDescription = stringResource(id = R.string.profile_photo_desc), modifier = Modifier.size(80.dp).clip(CircleShape))
        } else {
            Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, Modifier.size(40.dp)) }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(user?.displayName ?: stringResource(id = R.string.guest_user), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(user?.email ?: stringResource(id = R.string.not_logged_in), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))

        // --- ESTADÍSTICAS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(Icons.Default.Star, stringResource(id = R.string.favorites_title), favoriteCount.toString(), Modifier.weight(1f))
            StatCard(Icons.AutoMirrored.Filled.List, stringResource(id = R.string.queries_title), historyCount.toString(), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- PREFERENCIAS ---
        SectionTitle(stringResource(id = R.string.preferences_section))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(stringResource(id = R.string.dark_mode_title), modifier = Modifier.weight(1f))
                    Switch(checked = isDarkMode, onCheckedChange = onThemeChanged)
                }
                Text(stringResource(id = R.string.language_title), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                    listOf("es" to "ES", "en" to "EN", "pt" to "PT").forEach { (code, label) ->
                        FilterChip(selected = currentLanguage == code, onClick = { onLanguageChanged(code) }, label = { Text(label) })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SOPORTE ---
        SectionTitle(stringResource(id = R.string.info_support_section))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                MenuOptionItem(Icons.AutoMirrored.Filled.List, stringResource(id = R.string.scan_history_title), onNavigateToHistory)
                MenuOptionItem(Icons.Default.BarChart, stringResource(id = R.string.my_stats_title), onNavigateToStats)
                MenuOptionItem(Icons.Default.Help, stringResource(id = R.string.help_center_title), onNavigateToHelp)
                MenuOptionItem(Icons.Default.Info, stringResource(id = R.string.terms_conditions_title), onNavigateToTerms)
                MenuOptionItem(Icons.Default.ContactSupport, stringResource(id = R.string.contact_center_title), onNavigateToContact)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        TextButton(onClick = { auth.signOut(); onNavigateToLogin() }) {
            Text(stringResource(id = R.string.sign_out_button), color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth())
}

@Composable
fun MenuOptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}
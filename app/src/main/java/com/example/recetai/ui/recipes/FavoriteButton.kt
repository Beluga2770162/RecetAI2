package com.example.recetai.ui.recipes

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.recetai.R

/**
 * Componente de favorito profesional con animación de resorte (spring).
 * Utiliza un tono verde esmeralda (0xFF10B981) para una experiencia visual moderna.
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animación de escala para feedback táctil instantáneo
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fav_scale"
    )

    IconButton(
        onClick = onClick,
        modifier = modifier.scale(scale)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            // Internacionalización para accesibilidad (TalkBack)
            contentDescription = if (isFavorite)
                stringResource(id = R.string.remove_favorite_desc)
            else
                stringResource(id = R.string.add_favorite_desc),
            tint = if (isFavorite) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}
package com.example.carelyze.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Science
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Icons for scanner types — vector glyphs instead of emoji for a consistent clinical UI.
 */
fun scannerIconForId(scannerId: String): ImageVector =
    when (scannerId) {
        "skin" -> Icons.Outlined.Healing
        "lung" -> Icons.Outlined.Air
        "brain" -> Icons.Outlined.Psychology
        "alzheimer" -> Icons.Outlined.Science
        else -> Icons.Outlined.Healing
    }

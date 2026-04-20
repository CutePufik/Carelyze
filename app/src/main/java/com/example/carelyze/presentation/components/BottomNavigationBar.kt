package com.example.carelyze.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NavSurface = Color(0xFF0F172A)
private val NavAccent = Color(0xFF5C6CFF)
private val NavMuted = Color(0xFFF8FAFC).copy(alpha = 0.58f)

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigateToScanner: () -> Unit,
    onNavigateToAdvice: () -> Unit
) {
    NavigationBar(
        containerColor = NavSurface,
        tonalElevation = 0.dp,
    ) {
        NavigationBarItem(
            selected = currentRoute == "scanner",
            onClick = onNavigateToScanner,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.MedicalServices,
                    contentDescription = null,
                )
            },
            label = { Text("Сканер", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavAccent,
                selectedTextColor = NavAccent,
                indicatorColor = NavAccent.copy(alpha = 0.14f),
                unselectedIconColor = NavMuted,
                unselectedTextColor = NavMuted,
            )
        )
        NavigationBarItem(
            selected = currentRoute == "advice",
            onClick = onNavigateToAdvice,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Article,
                    contentDescription = null,
                )
            },
            label = { Text("Советы", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavAccent,
                selectedTextColor = NavAccent,
                indicatorColor = NavAccent.copy(alpha = 0.14f),
                unselectedIconColor = NavMuted,
                unselectedTextColor = NavMuted,
            )
        )
    }
}

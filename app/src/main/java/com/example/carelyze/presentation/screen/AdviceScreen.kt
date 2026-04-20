package com.example.carelyze.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.carelyze.presentation.components.BottomNavigationBar
import com.example.carelyze.presentation.viewmodel.AdviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onAdviceClick: (String) -> Unit,
    onNavigateToScanner: () -> Unit
) {
    val viewModel: AdviceViewModel = viewModel(factory = viewModelFactory)
    val advices by viewModel.advices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Советы", color = Color(0xFFF8FAFC)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "advice",
                onNavigateToScanner = onNavigateToScanner,
                onNavigateToAdvice = {}
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Categories (scroll on narrow screens)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryChip(
                    text = "Питание",
                    isSelected = selectedCategory == "Питание",
                    onClick = { viewModel.loadAdvices("Питание") }
                )
                CategoryChip(
                    text = "Ментальное здоровье",
                    isSelected = selectedCategory == "Ментальное здоровье",
                    onClick = { viewModel.loadAdvices("Ментальное здоровье") }
                )
                CategoryChip(
                    text = "Сон",
                    isSelected = selectedCategory == "Сон",
                    onClick = { viewModel.loadAdvices("Сон") }
                )
            }
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5C6CFF))
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error!!, color = Color(0xFFFF5252))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(advices) { advice ->
                        AdviceCard(
                            advice = advice,
                            onClick = { onAdviceClick(advice.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF5C6CFF),
            containerColor = Color(0xFF1E293B),
            selectedLabelColor = Color(0xFFF8FAFC),
            labelColor = Color(0xFF5C6CFF)
        )
    )
}



fun String.fixAdviceImageUrl(apiBaseUrl: String): String {
    val base = apiBaseUrl.trimEnd('/')
    if (!contains("127.0.0.1") && !contains("localhost", ignoreCase = true)) {
        return this
    }
    val origin = base.toUri()
    val uri = this.toUri()
    return uri.buildUpon()
        .scheme(origin.scheme)
        .encodedAuthority(origin.encodedAuthority)
        .build()
        .toString()
}

@Composable
fun AdviceCard(
    advice: com.example.carelyze.domain.model.Advice,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF334155)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (!advice.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = advice.imageUrl,
                    contentDescription = advice.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color(0xFF334155)),
                    error = ColorPainter(Color(0xFF450A0A))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = advice.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8FAFC)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = advice.description,
                    fontSize = 14.sp,
                    color = Color(0xFFF8FAFC).copy(alpha = 0.7f),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = advice.category,
                        fontSize = 12.sp,
                        color = Color(0xFF5C6CFF)
                    )
                    if (advice.readingTime != null) {
                        Text(
                            text = "• ${advice.readingTime} мин",
                            fontSize = 12.sp,
                            color = Color(0xFFF8FAFC).copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

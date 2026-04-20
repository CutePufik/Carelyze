package com.example.carelyze.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.carelyze.presentation.viewmodel.AdviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceDetailScreen(
    adviceId: String,
    viewModelFactory: ViewModelProvider.Factory,
    onBack: () -> Unit
) {
    val viewModel: AdviceViewModel = viewModel(factory = viewModelFactory)
    val selectedAdvice by viewModel.selectedAdvice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(adviceId) {
        viewModel.selectAdvice(adviceId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Совет", color = Color(0xFFF8FAFC)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color(0xFFF8FAFC))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(adviceId) }) {
                        Icon(
                            if (selectedAdvice?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (selectedAdvice?.isFavorite == true) Color(0xFFFF5252) else Color(0xFFF8FAFC)
                        )
                    }
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться", tint = Color(0xFFF8FAFC))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5C6CFF))
            }
        } else {
            selectedAdvice?.let { advice ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (!advice.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = advice.imageUrl,
                            contentDescription = advice.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(Color(0xFF334155)),
                            error = ColorPainter(Color(0xFF450A0A))
                        )
                    }
                    
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = advice.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = advice.category,
                                fontSize = 14.sp,
                                color = Color(0xFF5C6CFF)
                            )
                            if (advice.readingTime != null) {
                                Text(
                                    text = "• ${advice.readingTime} мин",
                                    fontSize = 14.sp,
                                    color = Color(0xFFF8FAFC).copy(alpha = 0.5f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = advice.content ?: advice.description,
                            fontSize = 16.sp,
                            color = Color(0xFFF8FAFC).copy(alpha = 0.9f),
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}

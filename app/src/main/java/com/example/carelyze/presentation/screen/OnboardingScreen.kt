package com.example.carelyze.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carelyze.presentation.viewmodel.OnboardingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    val slides = viewModel.slides
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(0) }
    
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            currentPage = index
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        LazyRow(
            state = listState,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(slides.size) { index ->
                Box(modifier = Modifier.fillParentMaxWidth()) {
                    OnboardingPage(slide = slides[index])
                }
            }
        }
        
        // Progress dots
        Row(
            modifier = Modifier.padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            slides.forEachIndexed { index, _ ->
                val isSelected = currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 40.dp else 12.dp, 12.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color(0xFF5C6CFF)
                            else Color(0xFF5C6CFF).copy(alpha = 0.3f)
                        )
                )
            }
        }
        
        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onComplete,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFFF8FAFC).copy(alpha = 0.8f)
                )
            ) {
                Text("Пропустить")
            }
            
            Button(
                onClick = {
                    if (currentPage < slides.size - 1) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(currentPage + 1)
                        }
                    } else {
                        onComplete()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5C6CFF)
                )
            ) {
                Text(
                    if (currentPage == slides.size - 1) "Начать сканирование" else "Далее"
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(slide: com.example.carelyze.presentation.viewmodel.OnboardingSlide) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = slide.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF8FAFC),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = slide.description,
            fontSize = 16.sp,
            color = Color(0xFFF8FAFC).copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

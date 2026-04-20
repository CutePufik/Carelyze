package com.example.carelyze.presentation.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carelyze.presentation.components.scannerIconForId
import com.example.carelyze.presentation.viewmodel.ScanProcessViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanProcessScreen(
    scannerId: String,
    viewModelFactory: ViewModelProvider.Factory,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ScanProcessViewModel = viewModel(factory = viewModelFactory)
    val selectedScanner by viewModel.selectedScanner.collectAsState()
    val scanResult by viewModel.scanResult.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val error by viewModel.error.collectAsState()

    var cameraCaptureUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraCaptureUri?.let { uri -> viewModel.analyzeImage(uri.toString()) }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.analyzeImage(it.toString()) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createCameraImageUri(context) ?: return@rememberLauncherForActivityResult
            cameraCaptureUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    LaunchedEffect(scannerId) {
        viewModel.loadScannerById(scannerId)
    }

    fun openCamera() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED -> {
                val uri = createCameraImageUri(context) ?: return
                cameraCaptureUri = uri
                takePictureLauncher.launch(uri)
            }
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun openGallery() {
        pickImageLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сканирование", color = Color(0xFFF8FAFC)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color(0xFFF8FAFC))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            selectedScanner?.let { scanner ->
                val accent = Color(scanner.color)
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(accent.copy(alpha = 0.18f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = scannerIconForId(scanner.id),
                        contentDescription = scanner.title,
                        tint = accent,
                        modifier = Modifier.size(52.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = scanner.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8FAFC)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = scanner.hint,
                    fontSize = 16.sp,
                    color = Color(0xFFF8FAFC).copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { openCamera() },
                        enabled = !isAnalyzing,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5C6CFF)
                        )
                    ) {
                        Text("Сделать фото")
                    }
                    Button(
                        onClick = { openGallery() },
                        enabled = !isAnalyzing,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5C6CFF)
                        )
                    ) {
                        Text("Галерея")
                    }
                }

                error?.let { message ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF450A0A).copy(alpha = 0.55f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                color = Color(0xFFFFCDD2)
                            )
                            TextButton(onClick = { viewModel.clearError() }) {
                                Text("OK", color = Color(0xFF5C6CFF))
                            }
                        }
                    }
                }

                if (isAnalyzing) {
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(color = Color(0xFF5C6CFF))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Анализ изображения...",
                        color = Color(0xFFF8FAFC).copy(alpha = 0.7f)
                    )
                }

                scanResult?.let { result ->
                    Spacer(modifier = Modifier.height(32.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E293B)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "${result.diagnosis} · ${result.probability}%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF8FAFC)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = result.description,
                                fontSize = 14.sp,
                                color = Color(0xFFF8FAFC).copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = result.recommendations,
                                fontSize = 14.sp,
                                color = Color(0xFFF8FAFC).copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun createCameraImageUri(context: Context): Uri? {
    return try {
        val file = File(context.cacheDir, "carelyze_capture_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (_: Exception) {
        null
    }
}

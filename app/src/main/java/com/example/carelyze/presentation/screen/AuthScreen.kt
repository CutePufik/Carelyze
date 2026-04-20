package com.example.carelyze.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carelyze.presentation.viewmodel.AuthViewModel

private val AuthFieldColors @Composable get() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFFF8FAFC),
    unfocusedTextColor = Color(0xFFF8FAFC),
    disabledTextColor = Color(0xFFF8FAFC).copy(alpha = 0.38f),
    focusedBorderColor = Color(0xFF5C6CFF),
    unfocusedBorderColor = Color(0xFF334155),
    cursorColor = Color(0xFF5C6CFF),
    focusedLabelColor = Color(0xFF94A3B8),
    unfocusedLabelColor = Color(0xFF64748B),
    focusedContainerColor = Color(0xFF1E293B),
    unfocusedContainerColor = Color(0xFF1E293B),
)

@Composable
fun AuthScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onAuthSuccess: () -> Unit,
) {
    val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val isLoginMode by viewModel.isLoginMode.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) onAuthSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (isLoginMode) "Вход" else "Регистрация",
            color = Color(0xFFF8FAFC),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = if (isLoginMode) "Введите email и пароль" else "Создайте аккаунт",
            color = Color(0xFFF8FAFC).copy(alpha = 0.75f),
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isLoginMode) {
            OutlinedTextField(
                value = fullName,
                onValueChange = viewModel::onFullNameChange,
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = AuthFieldColors,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = AuthFieldColors,
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = AuthFieldColors,
            singleLine = true,
        )

        if (!error.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error.orEmpty(),
                color = Color(0xFFFF6B6B),
                fontSize = 13.sp,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = viewModel::submit,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6CFF)),
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(if (isLoginMode) "Войти" else "Зарегистрироваться")
            }
        }

        TextButton(
            onClick = {
                viewModel.setLoginMode(!isLoginMode)
                viewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (isLoginMode) "Нет аккаунта? Зарегистрироваться" else "Уже есть аккаунт? Войти",
                color = Color(0xFFF8FAFC),
            )
        }
    }
}

package com.chefkiss.app.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.chefkiss.app.ui.theme.CoralPrimary
import androidx.compose.material3.MaterialTheme
import com.chefkiss.app.ui.theme.TextPrimary

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLogin: (String, String) -> Pair<Boolean, Map<String, String>>
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo borroso
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1643101570532-88c8ecc07c1f"),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(8.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.4f
        )

        // overlay oscuro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = TextPrimary.copy(alpha = 0.9f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // logo emoji
                    Text(
                        text = "👨‍🍳",
                        fontSize = 56.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // título
                    Text(
                        text = "ChefKiss",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Tu comunidad foodie verificada",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // campo email
                    Column {
                        Text(
                            text = "Email",
                            fontSize = 13.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("tu@email.com") },
                            isError = errors.containsKey("email"),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.95f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                                errorContainerColor = Color.White.copy(alpha = 0.95f),
                                focusedBorderColor = Color.White.copy(alpha = 0.2f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        errors["email"]?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // campo password
                    Column {
                        Text(
                            text = "Contraseña",
                            fontSize = 13.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••") },
                            isError = errors.containsKey("password"),
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.95f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                                errorContainerColor = Color.White.copy(alpha = 0.95f),
                                focusedBorderColor = Color.White.copy(alpha = 0.2f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                            ),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (showPassword) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        errors["password"]?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // botón login
                    Button(
                        onClick = {
                            val (success, validationErrors) = onLogin(email, password)
                            errors = validationErrors
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoralPrimary
                        )
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // link crear cuenta
                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = "Crear cuenta",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
package com.chefkiss.app.ui.screens.profile

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chefkiss.app.data.model.Review
import com.chefkiss.app.data.model.User
import com.chefkiss.app.ui.components.BottomBar
import com.chefkiss.app.ui.navigation.Screen
import com.chefkiss.app.ui.theme.*

@Composable
fun ProfileScreen(
    user: User?,
    reviews: List<Review>,
    cameraImage: Uri?,
    onNavigateToMyReviews: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                currentScreen = Screen.Profile,
                onNavigate = onNavigate
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header gradient
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                color = CoralPrimary
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier
                            .padding(start = 24.dp, bottom = 12.dp)
                            .size(96.dp),
                        shape = CircleShape,
                        color = AccentLight,
                        border = androidx.compose.foundation.BorderStroke(4.dp, Background),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🙋", fontSize = 48.sp)
                        }
                    }
                }
            }

            // User info
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column {
                    Text(
                        text = user?.name ?: "",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = user?.email ?: "",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CoralPrimary.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CoralPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Crítica verificada",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = CoralPrimary
                            )
                        }
                    }
                }

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        Triple(reviews.filter { it.userName == user?.name || it.userName == "Tú" }.size.toString(), "Reseñas", CoralPrimary),
                        Triple(if (cameraImage != null) "1" else "0", "Verificadas", CoralPrimary),
                        Triple("4.8", "Rating", TextPrimary)
                    ).forEach { (value, label, color) ->
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = value,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // Menu options
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileMenuItem(
                        title = "Tus reseñas publicadas",
                        subtitle = "Ver todas tus opiniones",
                        onClick = onNavigateToMyReviews
                    )

                    ProfileMenuItem(
                        title = "Editar perfil",
                        subtitle = "Actualiza tu información",
                        onClick = onNavigateToEditProfile
                    )

                    ProfileMenuItem(
                        title = "Configuración",
                        subtitle = "Privacidad y notificaciones",
                        onClick = onNavigateToSettings
                    )

                    Surface(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Cerrar sesión",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "Salir de tu cuenta",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                )
                            }
                            Text(text = "→", fontSize = 20.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
            Text(text = "→", fontSize = 20.sp, color = CoralPrimary)
        }
    }
}

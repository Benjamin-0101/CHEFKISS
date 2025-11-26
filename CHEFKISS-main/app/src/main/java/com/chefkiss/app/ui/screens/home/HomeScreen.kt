package com.chefkiss.app.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chefkiss.app.data.model.User
import com.chefkiss.app.ui.components.BottomBar
import com.chefkiss.app.ui.components.TopBar
import com.chefkiss.app.ui.navigation.Screen
import com.chefkiss.app.ui.theme.*
import androidx.compose.material3.MaterialTheme

@Composable
fun HomeScreen(
    user: User?,
    cameraImage: Uri?,
    onNavigateToRestaurant: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Inicio",
                showMenu = true,
                onMenuClick = onNavigateToSettings
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = Screen.Home,
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // tarjeta bienvenida
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = AccentLight,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    CoralPrimary.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Hola, ${user?.name ?: "foodie"} 🍽️",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (cameraImage != null) {
                            "Tu última visita fue verificada hace 2 días"
                        } else {
                            "Verifica tu próxima visita con una foto"
                        },
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            // restaurante destacado
            Column {
                Text(
                    text = "Destacado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentBorder)
                ) {
                    Column {
                        // imagen
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(192.dp)
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1758275682464-ddd906bf34fe",
                                contentDescription = "Restaurant",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // rating badge
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp),
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = StarYellow,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "4.5",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        // info
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "La Casona Gourmet",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Santiago Centro, Chile",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = onNavigateToRestaurant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoralPrimary
                                )
                            ) {
                                Text(
                                    text = "Ver reseñas",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // actividad reciente
            Column {
                Text(
                    text = "Tu actividad reciente",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        Triple("La Casona Gourmet", "Hace 2 días", cameraImage != null),
                        Triple("Bistró Francés", "Hace 1 semana", true),
                        Triple("Sushi House", "Hace 2 semanas", false)
                    ).forEach { (name, date, verified) ->
                        Surface(
                            onClick = onNavigateToRestaurant,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 1.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                AccentBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = date,
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }

                                if (verified) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verificado",
                                        tint = CoralPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
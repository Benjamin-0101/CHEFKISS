package com.chefkiss.app.ui.screens.restaurant

import android.net.Uri

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.chefkiss.app.data.model.MenuItem
import com.chefkiss.app.data.model.Review
import com.chefkiss.app.ui.components.BottomBar
import com.chefkiss.app.ui.components.ReviewCard
import com.chefkiss.app.ui.components.TopBar
import com.chefkiss.app.ui.navigation.Screen
import com.chefkiss.app.ui.theme.*

private val GlassWhite = Color(0x66FFFFFF)

@Composable
fun RestaurantScreen(
    reviews: List<Review>,
    cameraImage: Uri?,
    onBack: () -> Unit,
    onPublishReview: (String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    // Estado para diálogo de reseña
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(5) }

    // Restaurante destacado (datos mock premium)
    val restaurant = remember {
        RestaurantData(
            name = "La Casona Gourmet",
            description = "Restaurante de alta cocina fusión latinoamericana-mediterránea. " +
                    "Ingredientes orgánicos de temporada y técnicas innovadoras en un ambiente sofisticado.",
            address = "Av. Nueva Providencia 1881, Local 34",
            city = "Providencia, Santiago",
            cuisine = "Fusión Latina-Mediterránea",
            priceRange = "$$$$ • $45.000 - $85.000 CLP",
            rating = 4.8,
            reviewCount = 248,
            phoneNumber = "+56 2 2234 5678",
            website = "www.lacasonagourmet.cl",
            hours = "Lun-Sáb: 12:30-15:30, 19:00-23:00 • Dom: 12:30-16:00",
            features = listOf(
                "🅿️ Estacionamiento",
                "🍷 Bar completo",
                "🎵 Música en vivo",
                "♿ Accesible",
                "👨‍👩‍👧‍👦 Apto familias",
                "🎂 Eventos privados"
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
                "https://images.unsplash.com/photo-1514933651103-005eec06c04b",
                "https://images.unsplash.com/photo-1414235077428-338989a2e8c0"
            ),
            menuHighlights = listOf(
                MenuItem(
                    name = "Salmón a la Plancha Premium",
                    description = "Salmón atlántico sobre risotto de espárragos y reducción de maracuyá",
                    price = "$32.900"
                ),
                MenuItem(
                    name = "Cordero Patagónico",
                    description = "Carré de cordero con puré trufado y vegetales asados",
                    price = "$38.500"
                ),
                MenuItem(
                    name = "Ravioles de Langostinos",
                    description = "Pasta fresca rellena con langostinos en salsa de azafrán",
                    price = "$29.900"
                ),
                MenuItem(
                    name = "Tiramisu de Autor",
                    description = "Tiramisu con café colombiano y amaretto",
                    price = "$8.900"
                )
            )
        )
    }

    // Reseñas mock eliminadas; ahora se reciben como parámetro

    Scaffold(
        topBar = {
            TopBar(
                title = "Restaurante",
                showBack = true,
                onBackClick = onBack
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = Screen.Restaurant,
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
            // Hero Image con overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = restaurant.images[0],
                    contentDescription = "Restaurant Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    TextPrimary.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // Rating badge (top-right)
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = GlassWhite,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = restaurant.rating.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "(${restaurant.reviewCount})",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Restaurant name (bottom-left)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = restaurant.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = restaurant.cuisine,
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Información básica
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = restaurant.description,
                            fontSize = 15.sp,
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )

                        Divider(color = AccentBorder)

                        // Dirección
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            title = "Dirección",
                            content = "${restaurant.address}\n${restaurant.city}"
                        )

                        // Teléfono
                        InfoRow(
                            icon = Icons.Default.Phone,
                            title = "Teléfono",
                            content = restaurant.phoneNumber
                        )

                        // Horario
                        InfoRow(
                            icon = Icons.Default.Schedule,
                            title = "Horario",
                            content = restaurant.hours
                        )

                        // Precio
                        InfoRow(
                            icon = Icons.Default.AttachMoney,
                            title = "Rango de precio",
                            content = restaurant.priceRange
                        )
                    }
                }

                // Features/Características
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Características",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        restaurant.features.chunked(3).forEach { column ->
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                column.forEach { feature ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = AccentLight
                                    ) {
                                        Text(
                                            text = feature,
                                            fontSize = 12.sp,
                                            color = TextPrimary,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Menú destacado
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Platos destacados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    restaurant.menuHighlights.forEach { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 1.dp,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.description,
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                                Text(
                                    text = item.price,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralPrimary,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }

                // Galería de fotos
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Galería",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        restaurant.images.take(3).forEach { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Gallery",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Botón de verificar visita
                Button(
                    onClick = { onNavigate(Screen.Camera) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoralPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Verificar visita con foto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Reseñas
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reseñas (${reviews.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        TextButton(onClick = { showReviewDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Escribir",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    reviews.forEach { review ->
                        ReviewCard(review = review)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Diálogo para escribir reseña
        if (showReviewDialog) {
            Dialog(onDismissRequest = { showReviewDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Escribe tu reseña",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        // Rating selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { selectedRating = star }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (star <= selectedRating) StarYellow else TextSecondary.copy(
                                            alpha = 0.3f
                                        ),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            placeholder = { Text("Cuéntanos sobre tu experiencia...") },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SurfaceVariant,
                                unfocusedContainerColor = SurfaceVariant
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    showReviewDialog = false
                                    reviewText = ""
                                    selectedRating = 5
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = {
                                    onPublishReview(reviewText)
                                    showReviewDialog = false
                                    reviewText = ""
                                    selectedRating = 5
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoralPrimary
                                )
                            ) {
                                Text("Publicar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = CoralPrimary.copy(alpha = 0.1f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CoralPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Text(
                text = content,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

// Data class auxiliar para el restaurante
private data class RestaurantData(
    val name: String,
    val description: String,
    val address: String,
    val city: String,
    val cuisine: String,
    val priceRange: String,
    val rating: Double,
    val reviewCount: Int,
    val phoneNumber: String,
    val website: String,
    val hours: String,
    val features: List<String>,
    val images: List<String>,
    val menuHighlights: List<MenuItem>
)

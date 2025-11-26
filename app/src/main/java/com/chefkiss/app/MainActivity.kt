package com.chefkiss.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chefkiss.app.ui.navigation.Screen
import com.chefkiss.app.ui.screens.assistant.AssistantScreen
import com.chefkiss.app.ui.screens.camera.CameraScreen
import com.chefkiss.app.ui.screens.home.HomeScreen
import com.chefkiss.app.ui.screens.login.LoginScreen
import com.chefkiss.app.ui.screens.profile.EditProfileScreen
import com.chefkiss.app.ui.screens.profile.MyReviewsScreen
import com.chefkiss.app.ui.screens.profile.ProfileScreen
import com.chefkiss.app.ui.screens.register.RegisterScreen
import com.chefkiss.app.ui.screens.restaurant.RestaurantScreen
import com.chefkiss.app.ui.screens.settings.SettingsScreen
import com.chefkiss.app.ui.theme.ChefKissTheme
import com.chefkiss.app.viewmodel.ChefKissViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChefKissTheme {
                val viewModel = remember { ChefKissViewModel(applicationContext) }
                ChefKissApp(viewModel)
            }
        }
    }
}

@Composable
fun ChefKissApp(viewModel: ChefKissViewModel) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val user by viewModel.user.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val cameraImage by viewModel.cameraImage.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val emailNotifications by viewModel.emailNotifications.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val dailyMeal by viewModel.dailyMeal.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val startDestination = if (user != null) Screen.Home.route else Screen.Login.route

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        },
                        onLogin = { email, password ->
                            scope.launch {
                                val (success, errors) = viewModel.login(email, password)
                                if (success && errors.isEmpty()) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                } else {
                                    val msg = errors.values.firstOrNull()
                                        ?: "Credenciales inválidas o usuario no registrado."
                                    viewModel.showMessage(msg)
                                }
                            }
                            Pair(false, emptyMap())
                        }
                    )
                }

                composable(Screen.Register.route) {
                    val context = LocalContext.current
                    RegisterScreen(
                        onRegisterSuccess = {
                            android.widget.Toast.makeText(context, "Registro OK: Yendo al Login", android.widget.Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        },
                        onRegister = { name, email, phone, password, confirmPassword ->
                            scope.launch {
                                val (success, errors) =
                                    viewModel.register(name, email, phone, password, confirmPassword)
                                if (success) {
                                    android.widget.Toast.makeText(context, "Registro OK: Yendo al Login", android.widget.Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Register.route) { inclusive = true }
                                    }
                                } else {
                                    val msg = errors.values.firstOrNull() ?: "Error al registrarse"
                                    viewModel.showMessage(msg)
                                }
                            }
                            Pair(false, emptyMap())
                        }
                    )
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        user = user,
                        dailyMeal = dailyMeal,
                        cameraImage = cameraImage,
                        onNavigateToRestaurant = {
                            navController.navigate(Screen.Restaurant.route)
                        },
                        onNavigateToSettings = {
                            navController.navigate(Screen.Settings.route)
                        },
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                if (screen == Screen.Home) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        }
                    )
                }

                composable(Screen.Restaurant.route) {
                    RestaurantScreen(
                        reviews = reviews,
                        cameraImage = cameraImage,
                        onBack = { navController.popBackStack() },
                        onPublishReview = { comment ->
                            viewModel.publishReview(comment)
                        },
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                if (screen == Screen.Home) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        }
                    )
                }

                composable(Screen.Camera.route) {
                    CameraScreen(
                        onClose = { navController.popBackStack() },
                        onPhotoTaken = { uri ->
                            viewModel.takePhoto(uri)
                            navController.popBackStack()
                            navController.navigate(Screen.Restaurant.route)
                        }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        user = user,
                        reviews = reviews,
                        cameraImage = cameraImage,
                        onNavigateToMyReviews = {
                            navController.navigate(Screen.MyReviews.route)
                        },
                        onNavigateToEditProfile = {
                            navController.navigate(Screen.EditProfile.route)
                        },
                        onNavigateToSettings = {
                            navController.navigate(Screen.Settings.route)
                        },
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                if (screen == Screen.Home) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        }
                    )
                }

                composable(Screen.Assistant.route) {
                    AssistantScreen(
                        onBack = { navController.popBackStack() },
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                if (screen == Screen.Home) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        }
                    )
                }

                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        user = user,
                        onBack = { navController.popBackStack() },
                        onSave = { name, phone, bio ->
                            viewModel.updateProfile(name, phone, bio)
                            navController.popBackStack()
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        notificationsEnabled = notificationsEnabled,
                        emailNotifications = emailNotifications,
                        onNotificationsChanged = { enabled ->
                            viewModel.setNotificationsEnabled(enabled)
                        },
                        onEmailNotificationsChanged = { enabled ->
                            viewModel.setEmailNotifications(enabled)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.MyReviews.route) {
                    MyReviewsScreen(
                        user = user,
                        reviews = reviews,
                        onBack = { navController.popBackStack() },
                        onNavigateToRestaurant = {
                            navController.navigate(Screen.Restaurant.route)
                        }
                    )
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
            )
        }
    }
}
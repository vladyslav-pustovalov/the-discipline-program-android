package my.vladpustovalov.thedisciplineprogram.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    if (viewModel.showingAlert) {
        AlertDialog(
            onDismissRequest = { viewModel.showingAlert = false },
            title = { Text("Authentication failed") },
            text = { Text(viewModel.errorMessage) },
            confirmButton = {
                TextButton(onClick = { viewModel.showingAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.03f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Welcome!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Email Field
                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    placeholder = { Text("Email", color = Color.Gray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .height(56.dp)
                )

                // Password Field
                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    placeholder = { Text("Password", color = Color.Gray) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .height(56.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Sign In Button
                val isButtonDisabled = viewModel.isLoginButtonDisabled
                val buttonBrush = if (!isButtonDisabled) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.2f),
                            Color.Gray.copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.5f),
                            Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(buttonBrush)
                        .clickable(enabled = !isButtonDisabled) {
                            viewModel.performLogin()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Sign In",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Social Media Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIconButton(
                    iconRes = R.drawable.ic_instagram,
                    contentDescription = "Instagram",
                    onClick = {
                        openUrl(
                            context = context,
                            appUrl = "instagram://user?username=the_discipline_program",
                            webUrl = "https://instagram.com/the_discipline_program"
                        )
                    }
                )

                SocialIconButton(
                    iconRes = R.drawable.ic_telegram,
                    contentDescription = "Telegram",
                    onClick = {
                        openUrl(
                            context = context,
                            appUrl = "tg://resolve?domain=the_discipline_channel",
                            webUrl = "https://t.me/the_discipline_channel"
                        )
                    }
                )

                SocialIconButton(
                    iconRes = R.drawable.ic_youtube,
                    contentDescription = "YouTube",
                    onClick = {
                        openUrl(
                            context = context,
                            appUrl = "youtube://@The_Discipline_Program/shorts",
                            webUrl = "https://www.youtube.com/@The_Discipline_Program/shorts"
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SocialIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(40.dp)
        )
    }
}

private fun openUrl(context: Context, appUrl: String, webUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
        context.startActivity(intent)
    } catch (_: Exception) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
        context.startActivity(intent)
    }
}

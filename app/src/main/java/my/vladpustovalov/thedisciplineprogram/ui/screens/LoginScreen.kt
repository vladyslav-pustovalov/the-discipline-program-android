package my.vladpustovalov.thedisciplineprogram.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.ui.navigation.Screen

@Composable
fun LoginRoute(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
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
            title = { Text(stringResource(R.string.login_auth_failed_title)) },
            text = { Text(viewModel.errorMessage.ifEmpty { stringResource(R.string.login_auth_failed_title) }) },
            confirmButton = {
                TextButton(onClick = { viewModel.showingAlert = false }) {
                    Text(stringResource(R.string.common_ok))
                }
            }
        )
    }

    LoginScreenContent(
        email = viewModel.email,
        password = viewModel.password,
        isLoading = viewModel.isLoading,
        isLoginButtonDisabled = viewModel.isLoginButtonDisabled,
        onEmailChange = { viewModel.email = it },
        onPasswordChange = { viewModel.password = it },
        onLoginClick = { viewModel.performLogin() }
    )
}

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LoginRoute(navController = navController, viewModel = viewModel)
}

@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    isLoginButtonDisabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = Color.Gray.copy(alpha = 0.03f)
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .defaultMinSize(minHeight = maxHeight)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = stringResource(R.string.login_welcome),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(
                    modifier = Modifier.padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    TextField(
                        value = email,
                        onValueChange = onEmailChange,
                        placeholder = { Text(stringResource(R.string.login_email_placeholder), color = Color.Gray) },
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

                    TextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = { Text(stringResource(R.string.login_password_placeholder), color = Color.Gray) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (!isLoginButtonDisabled) {
                                    onLoginClick()
                                }
                            }
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

                    val buttonBrush = if (!isLoginButtonDisabled) {
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
                            .clickable(enabled = !isLoginButtonDisabled) {
                                onLoginClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.login_sign_in_button),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialIconButton(
                            iconRes = R.drawable.ic_instagram,
                            contentDescription = stringResource(R.string.login_social_instagram_cd),
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
                            contentDescription = stringResource(R.string.login_social_telegram_cd),
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
                            contentDescription = stringResource(R.string.login_social_youtube_cd),
                            onClick = {
                                openUrl(
                                    context = context,
                                    appUrl = "youtube://@The_Discipline_Program/shorts",
                                    webUrl = "https://www.youtube.com/@The_Discipline_Program/shorts"
                                )
                            }
                        )
                    }
                }
            }
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
        val intent = Intent(Intent.ACTION_VIEW, appUrl.toUri())
        context.startActivity(intent)
    } catch (_: Exception) {
        val intent = Intent(Intent.ACTION_VIEW, webUrl.toUri())
        context.startActivity(intent)
    }
}

package my.vladpustovalov.tdp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.tdp.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.tdp.presentation.viewmodel.ChangePasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
) {
    if (changePasswordViewModel.showingAlert) {
        AlertDialog(
            onDismissRequest = { changePasswordViewModel.showingAlert = false },
            title = { Text("Something went wrong during password change") },
            text = { Text(changePasswordViewModel.errorMessage) },
            confirmButton = {
                TextButton(onClick = { changePasswordViewModel.showingAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change password", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (changePasswordViewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp)
                        )
                    } else {
                        TextButton(
                            onClick = {
                                changePasswordViewModel.saveNewPassword {
                                    authViewModel.signOut()
                                    navController.popBackStack()
                                }
                            },
                            enabled = !changePasswordViewModel.isSaveButtonDisabled
                        ) {
                            Text("Save", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = changePasswordViewModel.oldPassword,
                onValueChange = { changePasswordViewModel.oldPassword = it },
                label = { Text("Old Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = changePasswordViewModel.newPassword,
                onValueChange = { changePasswordViewModel.newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = changePasswordViewModel.confirmNewPassword,
                onValueChange = { changePasswordViewModel.confirmNewPassword = it },
                label = { Text("Confirm New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Validation Feedback
            when {
                changePasswordViewModel.oldPassword.isEmpty() || changePasswordViewModel.newPassword.isEmpty() -> {
                    // Empty feedback
                }
                changePasswordViewModel.isOldAndNewPasswordsTheSame -> {
                    Text(
                        text = "New password should be different",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !changePasswordViewModel.isNewPasswordConfirmed -> {
                    Text(
                        text = "New password is not confirmed",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !changePasswordViewModel.isValidPassword -> {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                else -> {
                    Text(
                        text = "All is good",
                        color = Color(0xFF2E7D32), // Green
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

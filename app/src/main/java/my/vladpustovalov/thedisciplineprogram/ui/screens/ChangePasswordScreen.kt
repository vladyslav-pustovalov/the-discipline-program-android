package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.ChangePasswordViewModel

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
            title = { Text(stringResource(R.string.change_password_error_title)) },
            text = { Text(changePasswordViewModel.errorMessage.ifEmpty { stringResource(R.string.change_password_error_title) }) },
            confirmButton = {
                TextButton(onClick = { changePasswordViewModel.showingAlert = false }) {
                    Text(stringResource(R.string.common_ok))
                }
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.change_password_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back_cd)
                        )
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
                            Text(stringResource(R.string.common_save), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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
                .consumeWindowInsets(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = changePasswordViewModel.oldPassword,
                onValueChange = { changePasswordViewModel.oldPassword = it },
                label = { Text(stringResource(R.string.change_password_old_password)) },
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
                label = { Text(stringResource(R.string.change_password_new_password)) },
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
                label = { Text(stringResource(R.string.change_password_confirm_new_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!changePasswordViewModel.isSaveButtonDisabled) {
                            changePasswordViewModel.saveNewPassword {
                                authViewModel.signOut()
                                navController.popBackStack()
                            }
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                changePasswordViewModel.oldPassword.isEmpty() || changePasswordViewModel.newPassword.isEmpty() -> {}
                changePasswordViewModel.isOldAndNewPasswordsTheSame -> {
                    Text(
                        text = stringResource(R.string.change_password_err_same),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !changePasswordViewModel.isNewPasswordConfirmed -> {
                    Text(
                        text = stringResource(R.string.change_password_err_not_confirmed),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !changePasswordViewModel.isValidPassword -> {
                    changePasswordViewModel.passwordValidationResId?.let { resId ->
                        Text(
                            text = stringResource(resId),
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    Text(
                        text = stringResource(R.string.change_password_all_good),
                        color = Color(0xFF2E7D32), // Green
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

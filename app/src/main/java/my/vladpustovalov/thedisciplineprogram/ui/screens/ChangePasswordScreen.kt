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

@Composable
fun ChangePasswordRoute(
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

    ChangePasswordScreenContent(
        oldPassword = changePasswordViewModel.oldPassword,
        newPassword = changePasswordViewModel.newPassword,
        confirmNewPassword = changePasswordViewModel.confirmNewPassword,
        isLoading = changePasswordViewModel.isLoading,
        isSaveButtonDisabled = changePasswordViewModel.isSaveButtonDisabled,
        isOldAndNewPasswordsTheSame = changePasswordViewModel.isOldAndNewPasswordsTheSame,
        isNewPasswordConfirmed = changePasswordViewModel.isNewPasswordConfirmed,
        isValidPassword = changePasswordViewModel.isValidPassword,
        passwordValidationResId = changePasswordViewModel.passwordValidationResId,
        onOldPasswordChange = { changePasswordViewModel.oldPassword = it },
        onNewPasswordChange = { changePasswordViewModel.newPassword = it },
        onConfirmNewPasswordChange = { changePasswordViewModel.confirmNewPassword = it },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {
            changePasswordViewModel.saveNewPassword {
                authViewModel.signOut()
                navController.popBackStack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreenContent(
    oldPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    isLoading: Boolean,
    isSaveButtonDisabled: Boolean,
    isOldAndNewPasswordsTheSame: Boolean,
    isNewPasswordConfirmed: Boolean,
    isValidPassword: Boolean,
    passwordValidationResId: Int?,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.change_password_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back_cd)
                        )
                    }
                },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp)
                        )
                    } else {
                        TextButton(
                            onClick = onSaveClick,
                            enabled = !isSaveButtonDisabled
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
                value = oldPassword,
                onValueChange = onOldPasswordChange,
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
                value = newPassword,
                onValueChange = onNewPasswordChange,
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
                value = confirmNewPassword,
                onValueChange = onConfirmNewPasswordChange,
                label = { Text(stringResource(R.string.change_password_confirm_new_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isSaveButtonDisabled) {
                            onSaveClick()
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                oldPassword.isEmpty() || newPassword.isEmpty() -> {}
                isOldAndNewPasswordsTheSame -> {
                    Text(
                        text = stringResource(R.string.change_password_err_same),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !isNewPasswordConfirmed -> {
                    Text(
                        text = stringResource(R.string.change_password_err_not_confirmed),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                !isValidPassword -> {
                    passwordValidationResId?.let { resId ->
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

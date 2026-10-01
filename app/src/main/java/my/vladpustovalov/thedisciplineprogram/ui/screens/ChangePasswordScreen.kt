package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.ChangePasswordViewModel
import my.vladpustovalov.thedisciplineprogram.ui.components.BackTopAppBar
import my.vladpustovalov.thedisciplineprogram.ui.components.ErrorAlertDialog
import my.vladpustovalov.thedisciplineprogram.ui.components.PasswordOutlinedTextField
import my.vladpustovalov.thedisciplineprogram.ui.theme.SuccessGreen

@Composable
fun ChangePasswordRoute(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
) {
    if (changePasswordViewModel.showingAlert) {
        ErrorAlertDialog(
            title = stringResource(R.string.change_password_error_title),
            message = changePasswordViewModel.errorMessage,
            onDismiss = { changePasswordViewModel.showingAlert = false }
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
            BackTopAppBar(
                title = stringResource(R.string.change_password_title),
                onBackClick = onBackClick,
                isLoading = isLoading,
                actionText = stringResource(R.string.common_save),
                onActionClick = onSaveClick,
                isActionEnabled = !isSaveButtonDisabled
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
            PasswordOutlinedTextField(
                value = oldPassword,
                onValueChange = onOldPasswordChange,
                label = stringResource(R.string.change_password_old_password),
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth()
            )

            PasswordOutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = stringResource(R.string.change_password_new_password),
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth()
            )

            PasswordOutlinedTextField(
                value = confirmNewPassword,
                onValueChange = onConfirmNewPasswordChange,
                label = stringResource(R.string.change_password_confirm_new_password),
                imeAction = ImeAction.Done,
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
                        color = SuccessGreen,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

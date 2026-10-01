package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.domain.state.UiState
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.EditUserViewModel
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.UserViewModel
import my.vladpustovalov.thedisciplineprogram.ui.components.BackTopAppBar
import my.vladpustovalov.thedisciplineprogram.ui.components.ErrorAlertDialog
import my.vladpustovalov.thedisciplineprogram.ui.navigation.Screen

@Composable
fun EditUserRoute(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    editUserViewModel: EditUserViewModel = hiltViewModel()
) {
    val userState by userViewModel.userState.collectAsState()

    LaunchedEffect(userState) {
        if (userState is UiState.Success) {
            val user = (userState as UiState.Success).data
            editUserViewModel.initUser(user)
        }
    }

    if (editUserViewModel.showingAlert) {
        ErrorAlertDialog(
            title = stringResource(R.string.edit_user_error_title),
            message = editUserViewModel.errorMessage,
            onDismiss = { editUserViewModel.showingAlert = false }
        )
    }

    EditUserScreenContent(
        firstName = editUserViewModel.firstName,
        lastName = editUserViewModel.lastName,
        phoneNumber = editUserViewModel.phoneNumber,
        dateOfBirth = editUserViewModel.dateOfBirth,
        isLoading = editUserViewModel.isLoading,
        onFirstNameChange = { editUserViewModel.firstName = it },
        onLastNameChange = { editUserViewModel.lastName = it },
        onPhoneNumberChange = { editUserViewModel.phoneNumber = it },
        onDateOfBirthChange = { editUserViewModel.dateOfBirth = it },
        onBackClick = { navController.popBackStack() },
        onChangePasswordClick = { navController.navigate(Screen.ChangePassword.route) },
        onSaveClick = {
            val currentUser = (userState as? UiState.Success)?.data
            currentUser?.let { user ->
                editUserViewModel.saveUpdatedUser(user) { updatedUser ->
                    userViewModel.updateUser(updatedUser)
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
fun EditUserScreenContent(
    firstName: String,
    lastName: String,
    phoneNumber: String,
    dateOfBirth: String,
    isLoading: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.edit_user_title),
                onBackClick = onBackClick,
                isLoading = isLoading,
                actionText = stringResource(R.string.common_save),
                onActionClick = onSaveClick
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.edit_user_details_header), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = { Text(stringResource(R.string.edit_user_first_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text(stringResource(R.string.edit_user_last_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text(stringResource(R.string.edit_user_phone_number)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = onDateOfBirthChange,
                label = { Text(stringResource(R.string.edit_user_date_of_birth)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSaveClick() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onChangePasswordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.edit_user_change_password), fontSize = 16.sp)
            }
        }
    }
}

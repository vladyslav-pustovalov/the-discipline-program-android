package my.vladpustovalov.tdp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.tdp.domain.state.UiState
import my.vladpustovalov.tdp.presentation.viewmodel.EditUserViewModel
import my.vladpustovalov.tdp.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
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
        AlertDialog(
            onDismissRequest = { editUserViewModel.showingAlert = false },
            title = { Text("Something went wrong during user update") },
            text = { Text(editUserViewModel.errorMessage) },
            confirmButton = {
                TextButton(onClick = { editUserViewModel.showingAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit User", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (editUserViewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp)
                        )
                    } else {
                        TextButton(
                            onClick = {
                                val currentUser = (userState as? UiState.Success)?.data
                                currentUser?.let { user ->
                                    editUserViewModel.saveUpdatedUser(user) { updatedUser ->
                                        userViewModel.updateUser(updatedUser)
                                        navController.popBackStack()
                                    }
                                }
                            }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("User Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = editUserViewModel.firstName,
                onValueChange = { editUserViewModel.firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editUserViewModel.lastName,
                onValueChange = { editUserViewModel.lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editUserViewModel.phoneNumber,
                onValueChange = { editUserViewModel.phoneNumber = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editUserViewModel.dateOfBirth,
                onValueChange = { editUserViewModel.dateOfBirth = it },
                label = { Text("Date of Birth (YYYY-MM-DD)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

package my.vladpustovalov.tdp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.tdp.data.model.User
import my.vladpustovalov.tdp.domain.state.UiState
import my.vladpustovalov.tdp.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.tdp.presentation.viewmodel.UserViewModel
import my.vladpustovalov.tdp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray.copy(alpha = 0.3f))
                    ) {
                        TextButton(
                            onClick = { authViewModel.signOut() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Sign Out", color = Color.Red, fontSize = 14.sp)
                        }
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.EditUser.route) }) {
                        Text("Edit", fontSize = 16.sp)
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { navController.navigate(Screen.ChangePassword.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Change Password", fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val state = userState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}")
                    }
                }
                is UiState.Success -> {
                    val user = state.data
                    UserProfileDetails(user = user)
                }
            }
        }
    }
}

@Composable
private fun UserProfileDetails(user: User) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ProfileItem(label = "Email", value = user.username) }
        item { ProfileItem(label = "First name", value = user.firstName ?: "") }
        item { ProfileItem(label = "Last name", value = user.lastName ?: "") }
        item { ProfileItem(label = "Level", value = user.trainingLevel?.name ?: "") }
        item { ProfileItem(label = "Plan", value = user.userPlan?.name ?: "") }
        item { ProfileItem(label = "Birthday", value = user.dateOfBirth ?: "") }
        item { ProfileItem(label = "Phone", value = user.phoneNumber ?: "") }
    }
}

@Composable
private fun ProfileItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$label:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = value, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

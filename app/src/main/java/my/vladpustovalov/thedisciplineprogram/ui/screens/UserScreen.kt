package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.data.model.User
import my.vladpustovalov.thedisciplineprogram.domain.state.UiState
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.UserViewModel
import my.vladpustovalov.thedisciplineprogram.ui.components.FullScreenError
import my.vladpustovalov.thedisciplineprogram.ui.components.FullScreenLoading
import my.vladpustovalov.thedisciplineprogram.ui.navigation.Screen

@Composable
fun UserRoute(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val userState by userViewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    UserScreenContent(
        userState = userState,
        onSignOut = { authViewModel.signOut() },
        onEditUser = { navController.navigate(Screen.EditUser.route) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenContent(
    userState: UiState<User>,
    onSignOut: () -> Unit,
    onEditUser: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_profile_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray.copy(alpha = 0.3f))
                    ) {
                        TextButton(
                            onClick = onSignOut,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(stringResource(R.string.user_sign_out), color = Color.Red, fontSize = 14.sp)
                        }
                    }
                },
                actions = {
                    TextButton(onClick = onEditUser) {
                        Text(stringResource(R.string.user_edit), fontSize = 16.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (userState) {
                is UiState.Loading -> {
                    FullScreenLoading()
                }
                is UiState.Error -> {
                    FullScreenError(message = stringResource(R.string.user_error_prefix, userState.message))
                }
                is UiState.Success -> {
                    val user = userState.data
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
        item { ProfileItem(label = stringResource(R.string.user_label_email), value = user.username) }
        item { ProfileItem(label = stringResource(R.string.user_label_first_name), value = user.firstName ?: "") }
        item { ProfileItem(label = stringResource(R.string.user_label_last_name), value = user.lastName ?: "") }
        item { ProfileItem(label = stringResource(R.string.user_label_level), value = user.trainingLevel?.name ?: "") }
        item { ProfileItem(label = stringResource(R.string.user_label_plan), value = user.userPlan?.name ?: "") }
        item { ProfileItem(label = stringResource(R.string.user_label_birthday), value = user.dateOfBirth ?: "") }
        item { ProfileItem(label = stringResource(R.string.user_label_phone), value = user.phoneNumber ?: "") }
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

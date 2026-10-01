package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel

@Composable
fun MainRoute(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    MainScreenContent(
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        navController = navController,
        authViewModel = authViewModel
    )
}

@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    MainRoute(navController = navController, authViewModel = authViewModel)
}

@Composable
fun MainScreenContent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.nav_program)) },
                    label = { Text(stringResource(R.string.nav_program)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = stringResource(R.string.nav_user)) },
                    label = { Text(stringResource(R.string.nav_user)) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> ProgramRoute(navController = navController)
                1 -> UserRoute(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}

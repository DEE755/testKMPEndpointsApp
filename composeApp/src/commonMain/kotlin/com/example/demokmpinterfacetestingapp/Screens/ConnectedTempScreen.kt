package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.Navigation.Router
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import di.ServiceLocator.authRepository
import di.ServiceLocator.userRepository
import di.ServiceLocator.logInOutViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview

fun ConnectedTempScreen(viewModel: LogInOutViewModel =logInOutViewModel, navRouter: Router? = null) {
    val navRouter = navRouter ?: Router()
    //val viewModel = viewModel ?: remember{ LogInOutViewModel(authRepository, userRepository) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        viewModel.uiState.value.currentUser?.let { user ->
            Text(
                text = "Successfully Signed In as: ${user.email}.\n Welcome ${user.username}!",
                color = Color.Blue,
                fontSize = 24.sp
            )

            Button(
                onClick = {
                    viewModel.signOut()
                    navRouter.backToLoginScreen()

            }
            ) { Text("Sign Out")}
        }
    }
}



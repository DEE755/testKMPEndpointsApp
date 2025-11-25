
package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import di.ServiceLocator
import di.ServiceLocator.logInOutViewModel
import di.initAppContext

val logInOutVM: LogInOutViewModel by lazy {
    /*LogInOutViewModel(
        ServiceLocator.authRepository, ServiceLocator.userRepository, ServiceLocator.cloudFilesRepository,
        tokenProvider = ServiceLocator.tokenProvider
    )*/
    logInOutViewModel
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initAppContext(context = this)

        setContent {
            MainScreen(logInOutVM)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainScreen(logInOutVM)
}
package com.example.demokmpinterfacetestingapp.Screens

import androidx.compose.foundation.Image
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.components.UploadImageButton
import com.example.demokmpinterfacetestingapp.util.PickedImage
import di.ServiceLocator.authRepository
import di.ServiceLocator.userRepository
import di.ServiceLocator.cloudFilesRepository

import com.example.demokmpinterfacetestingapp.util.decodeImage



@Composable
fun AppSelectionScreen(viewModel: LogInOutViewModel?, navRouter: Router?) {
    val viewModel = viewModel ?: LogInOutViewModel(authRepository, userRepository, cloudFilesRepository)
    val navRouter = navRouter ?: Router(Screen.AppSelectionScreen)

    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(30.dp)) {
        Text("Hello ${uiState.currentUser?.username ?: ""}!")
        Row {
            Button(onClick = { navRouter.navigate(Screen.AppBrowseScreen) }) {
                Text("Browse Existing App")
            }
            Spacer(Modifier.weight(1f, true))
            Button({ navRouter.navigate(Screen.AppCreationScreen) }) {
                Text("Create a new App")
            }
        }
    }
    var selected by remember { mutableStateOf<PickedImage?>(null) }

    Row {
        Column(Modifier.padding(16.dp)) {
            UploadImageButton { img -> selected = img }


            Spacer(Modifier.height(12.dp))
            if (selected != null) {
                val bmp = remember(selected) { decodeImage(selected!!.bytes) }
                Image(bitmap = bmp, contentDescription = "Selected image", modifier = Modifier.size(120.dp))
                Text("Nom: ${selected!!.name ?: "-"}")
                Text("MIME: ${selected!!.mimeType ?: "-"}")
                Text("Taille: ${selected!!.bytes.size} octets")
            }
        }

        Button(
            onClick = {
                selected?.let { image ->
                    viewModel.uploadAppImage(
                        image = image,
                        folder = "app-images",
                        fileBasename = "app-logo"
                    )
                }
            },
            enabled = selected != null
        ) {
            Text("Upload to server")
        }

    }
}

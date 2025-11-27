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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.components.RecyclerScreen

import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.components.UploadImageButton
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.ui.showToast
import com.example.demokmpinterfacetestingapp.util.PickedImage

import com.example.demokmpinterfacetestingapp.util.decodeImage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import di.ServiceLocator.logInOutViewModel
import kotlinx.coroutines.runBlocking

//TODO(: change navRouter to imported by di serviceLocator)

@Composable
fun AppSelectionScreen(viewModel: LogInOutViewModel = logInOutViewModel, navRouter: Router?) {
    //val viewModel = viewModel ?: LogInOutViewModel(authRepository, userRepository, cloudFilesRepository)
    val navRouter = navRouter ?: Router(Screen.AppSelectionScreen)

    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(30.dp)) {
        Text("Hello ${uiState.currentUser?.username}") //with personal info: id:${uiState.currentUser?._id}, name:${uiState.currentUser?.googleUserInfo?.family_name} ${uiState.currentUser?.googleUserInfo?.given_name}, ${uiState.currentUser?.googleUserInfo?.picture}!")


        uiState.currentUser?.googleUserInfo?.picture?.let { imageUrl ->
            KamelImage(
                resource = asyncPainterResource(imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(180.dp).clip(CircleShape),
                onLoading = { progress ->
                    CircularProgressIndicator(progress = { progress })
                },
                onFailure = { exception ->
                    Text("Error loading image: ${exception.message}")
                }
            )
        }

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
                    viewModel.uploadUserImage(
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

    Button(onClick = {
        runBlocking {viewModel.logout()} //need to ensure logout is complete before navigating
        navRouter.navigate(Screen.LoginScreen) }) {
        Text("Log out")
    }

        val testList: MutableList<ListItem> = mutableListOf()

        for (i in 0..50) {
            val temp=ListItem(id = "$i", title = "Item number $i", thumbnailUrl = "https://via.placeholder.com/150")
            testList.add(temp)
        }

    val coroutineScope = rememberCoroutineScope()

        RecyclerScreen(testList, Modifier.padding(16.dp)) { item ->
            println("Clicked on item: ${item.id} - ${item.title}")
            coroutineScope.launch {
                showToast("Clicked on item: ${item.id} - ${item.title}")
            }
        }


}

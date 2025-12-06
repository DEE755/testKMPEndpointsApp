package com.example.demokmpinterfacetestingapp.Screens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import com.example.demokmpinterfacetestingapp.components.RecyclerScreen

import com.example.demokmpinterfacetestingapp.ui.showToast

import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.appSelectionViewModel
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.ViewModel.AppSelectionViewModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.ui.tooling.preview.Preview

//TODO(: change navRouter to imported by di serviceLocator)
//TODO(: create new viewModel for this screen instead of logInOutVM, userPrefs injected inside it and new logout function there)

@Preview
@Composable
fun AppSelectionScreen() {
val viewModel: AppSelectionViewModel = appSelectionViewModel
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    Column(modifier = Modifier.padding(30.dp)) {
        Spacer(Modifier.height(30.dp))
        Text("Welcome ${uiState.currentUser?.username} !") //with personal info: id:${uiState.currentUser?._id}, name:${uiState.currentUser?.googleUserInfo?.family_name} ${uiState.currentUser?.googleUserInfo?.given_name}, ${uiState.currentUser?.googleUserInfo?.picture}!")

        Spacer(Modifier.height(30.dp))
       Row {
           uiState.currentUser?.googleUserInfo?.picture?.let { imageUrl ->
               KamelImage(
                   resource = asyncPainterResource(imageUrl),
                   contentDescription = "Profile Picture",
                   modifier = Modifier.size(140.dp).clip(CircleShape),
                   onLoading = { progress ->
                       CircularProgressIndicator(progress = { progress })
                   },
                   onFailure = { exception ->
                       Text("Error loading image: ${exception.message}")
                   }
               )
           }
           Spacer(modifier = Modifier.size(60.dp))
           Button(onClick = {
               runBlocking { viewModel.logout() } //need to ensure logout is complete before navigating
               navRouter.navigate(Screen.LoginScreen)
           }) {
               Text("Log out")
           }

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


        //TODO(ADD A PROPER RECYCLER VIEW FOR THE APPS LIST WITH CORRECT IMAGES AND ALL THE DATA + onCLICK HANDLER TO OPEN THE APP)
        val appItemList: MutableList<ListItem> = mutableListOf()

        val appList: List<App> = uiState.userApps

        for (i in appList.indices) {
            appList[i].appIconURL = "https://picsum.photos/200/200?random=$i"
            val appItem = ListItem(id = appList[i]._id, title = appList[i].name, thumbnailUrl = appList[i].appIconURL)
            appItemList.add(appItem)
        }

        Text("My Apps : (${appItemList.size})")
        val coroutineScope = rememberCoroutineScope()

        RecyclerScreen(appItemList, Modifier.padding(16.dp)) { item ->
            println("Clicked on item: ${item.id} - ${item.title}")
            coroutineScope.launch {
                showToast("Clicked on item: ${item.id} - ${item.title}")
            }
        }




    }
}

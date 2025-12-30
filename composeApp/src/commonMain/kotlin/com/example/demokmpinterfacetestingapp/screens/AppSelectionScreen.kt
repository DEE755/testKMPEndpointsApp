package com.example.demokmpinterfacetestingapp.screens

import com.example.demokmpinterfacetestingapp.navigation.Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import com.example.demokmpinterfacetestingapp.components.RecyclerScreen
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.appSelectionViewModel
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.viewmodel.AppSelectionViewModel
import com.example.demokmpinterfacetestingapp.components.CardButton
import com.example.demokmpinterfacetestingapp.components.VectorialPlusButton
import org.jetbrains.compose.ui.tooling.preview.Preview



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppSelectionScreen() {
    val viewModel: AppSelectionViewModel = appSelectionViewModel
    val uiState by viewModel.uiState.collectAsState()
    val apps by viewModel.appRepo.apps.collectAsState()
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()

    if (connectionStatus.isConnected) {
        LaunchedEffect(true) {

                viewModel.updateUserApps()
        }
    }

    LaunchedEffect(connectionStatus.isSessionTerminated){
    if (connectionStatus.isSessionTerminated) {
        viewModel.logout()
        viewModel.sessionManager.setSessionTerminated(false)
    }
    }

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

        val query = remember { mutableStateOf("") }
        val isActive = remember { mutableStateOf(false) }

        SearchBar(
            query = query.value,
            onQueryChange = { query.value = it },
            onSearch = { isActive.value = false },
            active = isActive.value,
            onActiveChange = { isActive.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search apps") }
        ) { }


        //TODO(ADD A PROPER RECYCLER VIEW FOR THE APPS LIST WITH CORRECT IMAGES AND ALL THE DATA + onCLICK HANDLER TO OPEN THE APP)
        val appItemList = remember { mutableStateListOf<ListItem>() }

//Todo(Make better and launch from vm maube)
        LaunchedEffect(apps) {
            viewModel.setLoading(true)
            appItemList.apply {
                clear()
                addAll(apps.mapIndexed { i, app ->
                    if (app.appIconURL.isNullOrEmpty())
                    {
                        app.appIconURL = "https://picsum.photos/200/200?random=$i"
                    }

                    if (app.appBannerURL.isNullOrEmpty())
                    {
                        app.appBannerURL = "https://picsum.photos/200/200?random=$i"
                    }

                    ListItem(
                        id = app._id, title = app.name, logoUrl = app.appIconURL, color = app.color,
                        bannerUrl = app.appBannerURL
                    )
                })
            }
            viewModel.setLoading(false)
        }

        if (uiState.userApps.isEmpty()) {

            CardButton(onClick = { navRouter.navigate(Screen.AppCreationScreen)}, height = 400.dp
            ) {
                Text("You app stray is empty, hit the + to create your first app")
            }

            CardButton(onClick = { navRouter.navigate(Screen.AppCreationScreen) }, height = 600.dp
            ) {
                Text("The community apps go there, hit the + button to find a shared app")
            }
       }
       else {
           RecyclerScreen(
               appItemList, Modifier.padding(10.dp).height(700.dp))
           { item ->
            println("Clicked on item: ${item.id} - ${item.title}")

               viewModel.showToast("Clicked on item: ${item.id} - ${item.title}")


           }
       }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            VectorialPlusButton(
                onClick = { navRouter.navigate(Screen.AppCreationScreen) }
            )
        }
    }
}

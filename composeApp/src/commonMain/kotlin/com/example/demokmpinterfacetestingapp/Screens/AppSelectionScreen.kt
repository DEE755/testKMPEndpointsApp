package com.example.demokmpinterfacetestingapp.Screens

import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import com.example.demokmpinterfacetestingapp.components.RecyclerScreen
import com.example.demokmpinterfacetestingapp.ui.showToast
import kotlinx.coroutines.launch
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.appSelectionViewModel
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.ViewModel.AppSelectionViewModel
import com.example.demokmpinterfacetestingapp.components.CardButton
import org.jetbrains.compose.ui.tooling.preview.Preview


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


    Column {

        //TODO(ADD A PROPER RECYCLER VIEW FOR THE APPS LIST WITH CORRECT IMAGES AND ALL THE DATA + onCLICK HANDLER TO OPEN THE APP)
        val appItemList = remember { mutableStateListOf<ListItem>() }


        LaunchedEffect(apps) {
            viewModel.setLoading(true)
            appItemList.apply {
                clear()
                addAll(apps.mapIndexed { i, app ->
                    if (app.appIconURL.isNullOrEmpty())
                    {
                        app.appIconURL = "https://picsum.photos/200/200?random=$i"
                    }

                    ListItem(id = app._id, title = app.name, thumbnailUrl = app.appIconURL, color = app.color)
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




            Button(
                onClick = { navRouter.navigate(Screen.AppCreationScreen) }
            ) {
                Text("+")
            }

    }
}

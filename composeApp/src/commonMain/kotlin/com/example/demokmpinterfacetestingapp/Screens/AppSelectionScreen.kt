package com.example.demokmpinterfacetestingapp.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.ui.tooling.preview.Preview


//TODO(: change navRouter to imported by di serviceLocator)

@Preview
@Composable
fun AppSelectionScreen() {
    val viewModel: AppSelectionViewModel = appSelectionViewModel
    val uiState by viewModel.uiState.collectAsState()
    val apps by viewModel.appRepo.apps.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        coroutineScope.launch {
            viewModel.updateUserApps() }
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
                    app.appIconURL = "https://picsum.photos/200/200?random=$i"
                    ListItem(id = app._id, title = app.name, thumbnailUrl = app.appIconURL)
                })
            }
            viewModel.setLoading(false)
        }


        if (uiState.userApps.isEmpty()) {
           Text("No apps available. Please create a new app.", modifier = Modifier.padding(10.dp))
       }
       else {
           RecyclerScreen(
               appItemList, Modifier.padding(10.dp).height(700.dp))
           { item ->
            println("Clicked on item: ${item.id} - ${item.title}")
            coroutineScope.launch {
                showToast("Clicked on item: ${item.id} - ${item.title}")
            }
        }

        }


            Button(
                onClick = { navRouter.navigate(Screen.AppCreationScreen) }
            ) {
                Text("+")
            }



    }
}

package com.example.demokmpinterfacetestingapp.Screens.Menus

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import com.example.demokmpinterfacetestingapp.ui.showToast



private val viewModel = authViewModel



@Composable
fun LogoOpenCloseMenuButton(drawerState: androidx.compose.material3.DrawerState) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val imageUrl =uiState.currentUser?.avatarURL?: "none"

    imageUrl.let { imageUrl ->
        Spacer(modifier = Modifier.height(30.dp))
        KamelImage(
            resource = asyncPainterResource(imageUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(70.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { scope.launch { drawerState.close() } },
            onLoading = { progress ->
                CircularProgressIndicator(progress = { progress })
            },
            onFailure = { exception ->
                scope.launch { showToast(exception.message ?: "Something went wrong")}
                Button(onClick = {scope.launch {drawerState.close()} }){Text("Close")}
            }
        )
    }
}


@Composable
fun Drawer() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)



    scope.launch {if (drawerState.isOpen) drawerState.close() else drawerState.open()}



}



@Composable
fun TopBar(onMenuClick: () -> Unit) {

    val scope = rememberCoroutineScope()
    val drawerStateFlag= remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val viewModel = authViewModel

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }


    Column {
        Button({onMenuClick()}) { Text("Menu") }
    }


}


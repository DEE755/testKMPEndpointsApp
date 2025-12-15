package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.BeyondBoundsLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authViewModel
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import com.example.demokmpinterfacetestingapp.Screens.Menus.SignInSignUpSliderHost
import com.example.demokmpinterfacetestingapp.Screens.Menus.SliderConnected
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import com.example.demokmpinterfacetestingapp.ui.showToast




@Composable
fun GeneralDrawerScreen(regularScreen: @Composable () -> Unit,) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel = authViewModel
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    @Composable
    fun LogoOpenCloseMenuButton() {
        uiState.currentUser?.avatarURL?.let { imageUrl ->
            Spacer(modifier = Modifier.height(30.dp))
            KamelImage(
                resource = asyncPainterResource(imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(70.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable { scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() } },
                onLoading = { progress ->
                    CircularProgressIndicator(progress = { progress })
                },
                onFailure = { exception ->
                    scope.launch { showToast(exception.message ?: "Something went wrong") }
                    //Button(onClick = {scope.launch {drawerState.open()} }){Text("Menu")}
                }
            )
        }
        if (uiState.currentUser == null) {
            Button(onClick = {
                scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
            }) {
                Text(if (drawerState.isOpen) "Close" else "Menu")
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider( LocalLayoutDirection provides LayoutDirection.Ltr){
                ModalDrawerSheet {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text("Menu")
                            Spacer(modifier = Modifier.width(140.dp))
                            LogoOpenCloseMenuButton()

                        }
                        Spacer(modifier = Modifier.height(12.dp))



                        if (uiState.currentUser != null) {
                            SliderConnected()
                        } else {

                            SignInSignUpSliderHost()
                        }


                    }
                }
                    }
            },
            gesturesEnabled = true
        ) {


            Column(modifier = Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
                        }) {
                        Text("|||")

                         }

                //LogoOpenCloseMenuButton()
                Spacer(Modifier.height(30.dp))

            }

            regularScreen()
        }
    }
}


@Composable
fun WithDrawer(regularScreen: @Composable () -> Unit, drawerScreen: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel = authViewModel
    val uiState by viewModel.uiState.collectAsState()


    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    @Composable
    fun LogoOpenCloseMenuButton(){
        uiState.currentUser?.avatarURL?.let { imageUrl ->
        Spacer(modifier = Modifier.height(30.dp))
        KamelImage(
            resource = asyncPainterResource(imageUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(70.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() } },
            onLoading = { progress ->
                CircularProgressIndicator(progress = { progress })
            },
            onFailure = { exception ->
                scope.launch { showToast(exception.message ?: "Something went wrong")}
                //Button(onClick = {scope.launch {drawerState.open()} }){Text("Menu")}
            }
        )
    }
        if (uiState.currentUser == null) {
            Button(onClick = {
                scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() }
            }) {
                Text(if (drawerState.isOpen)"Close"  else "Menu")
            }
        }}

    CompositionLocalProvider( LocalLayoutDirection provides LayoutDirection.Rtl) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text("Menu")
                            Spacer(modifier = Modifier.width(140.dp))
                            LogoOpenCloseMenuButton()

                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        drawerScreen()

                    }
                }
            },
            gesturesEnabled = true
        ) {

            Spacer(modifier = Modifier.height(30.dp))
            Column(modifier = Modifier.padding(16.dp)) {


                LogoOpenCloseMenuButton()
                Spacer(Modifier.height(70.dp))

            }

            regularScreen()
        }
    }
    }


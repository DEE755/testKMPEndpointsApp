package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.util.PickedImage
import com.example.demokmpinterfacetestingapp.util.decodeImage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@Composable
    fun CardButton(onClick: () -> Unit, height: Dp = 0.dp, content: @Composable () -> Unit) {
        val boxModifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)

        if (height != 0.dp) {boxModifier.height(height)}

        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Box(
                boxModifier,
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }




@Composable
fun LogoButton(onClick: () -> Unit, imageUrl: String,) {
    KamelImage(
        resource = if (imageUrl != "none") asyncPainterResource(imageUrl) else asyncPainterResource("https://media.istockphoto.com/id/1495088043/vector/user-profile-icon-avatar-or-person-icon-profile-picture-portrait-symbol-default-portrait.jpg?s=612x612&w=0&k=20&c=dhV2p1JwmloBTOaGAtaA3AW1KSnjsdMt7-U_3EZElZ0="),
        contentDescription = "Profile Picture",
        modifier = Modifier.size(70.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Gray, CircleShape)
            .clickable { onClick() },
        onLoading = { progress ->
            CircularProgressIndicator(progress = { progress })
        },
        onFailure = { exception ->
            println(exception.message ?: "Something went wrong")
            //Button(onClick = {scope.launch {drawerState.open()} }){Text("Menu")}
        }
    )
}


@Composable
fun LogoButton(onClick: () -> Unit, pickedArray: PickedImage, shape : Shape = CircleShape) {
    pickedArray.let { image ->
        val bmp = remember(image) { decodeImage(image.bytes) }
        Image(
            bitmap = bmp,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(70.dp)
                .clip(shape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    }
}



@Composable
fun VectorialPlusButton(shape: Shape = CircleShape, onClick: () -> Unit) {
    //importing SVG file is problematic in multiplatform compose, so using a simple plus sign for now
    Surface(
        color = Color(0xFF2196F3),
        modifier = Modifier
            .size(70.dp)
            .clip(shape)
            .border(1.dp, Color(0xFF1976D2), shape)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "+",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = Color.White
            )
        }
    }
}


@Composable
fun VectorialBannerPlusButton(shape: Shape = CircleShape, onClick: () -> Unit) {
    //importing SVG file is problematic in multiplatform compose, so using a simple plus sign for now
    Surface(
        color = Color(0xFF2196F3),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape)
            .border(1.dp, Color(0xFF1976D2), shape)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "+",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = Color.White
            )
        }
    }
}


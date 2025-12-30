package com.example.demokmpinterfacetestingapp.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * CustomDrawerLayout - A KMP-compatible modal drawer that opens from RIGHT to LEFT with animation
 *
 * This implementation creates a drawer that slides from the RIGHT side of the screen.
 * The drawer is positioned on the right side and slides to the left with smooth animation.
 *
 * @param drawerContent The content to display inside the drawer (positioned on the right)
 * @param content The main screen content that appears in the center
 * @param drawerState The state of the drawer (Open/Closed)
 */
@Composable
fun CustomDrawerLayout(
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    drawerState: androidx.compose.material3.DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    // Animation: drawer slides from right (offset 320.dp) to left (offset 0.dp)
    val drawerOffsetFraction by animateFloatAsState(
        targetValue = if (drawerState.isOpen) 0f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "DrawerSlide"
    )

    // Main content
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }

        // Drawer overlay - slides from right to left with animation
        if (drawerState.isOpen || drawerOffsetFraction < 1f) {
            // Semi-transparent scrim (overlay)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f * (1f - drawerOffsetFraction)))
                    .clickable {
                        // Close drawer when clicking on overlay
                    }
            )

            // Drawer slides in from the right
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (320.dp * drawerOffsetFraction))
            ) {
                Spacer(modifier = Modifier.weight(1f))

                ModalDrawerSheet(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    drawerContent()
                }
            }
        }
    }
}

/**
 * Opens the drawer with animation
 * Use this function to programmatically open the drawer
 */
suspend fun openDrawer(drawerState: androidx.compose.material3.DrawerState) {
    drawerState.open()
}

/**
 * Closes the drawer with animation
 * Use this function to programmatically close the drawer
 */
suspend fun closeDrawer(drawerState: androidx.compose.material3.DrawerState) {
    drawerState.close()
}

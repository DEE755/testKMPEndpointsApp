package com.example.demokmpinterfacetestingapp.components
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable



@Composable
fun WizardScreen(pages : List<@Composable () -> Unit> = emptyList(), onFinish : ()->Unit) {
    val uiState by viewModel.uiState.collectAsState()

    var page by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(30.dp)) {

        pages.getOrNull(page)?.invoke()

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { if (page > 0) page-- }, enabled = page > 0) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(enabled = uiState.nextEnabled,
                onClick = {
                        if (page < pages.lastIndex) {
                            page++
                            viewModel.enableNext(false)
                        }

                    else run {
                    onFinish.invoke()
                }
                }
            ){
                Text(if (page == pages.lastIndex) "Finish" else "Next")

            }
        }
    }
}


package com.example.demokmpinterfacetestingapp.Screens
import androidx.compose.foundation.Image
import com.example.demokmpinterfacetestingapp.components.WizardScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.components.ColorSelector
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.wizardViewModel
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.Model.models.Module.Module
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import com.example.demokmpinterfacetestingapp.ViewModel.AppWizardViewModel
import com.example.demokmpinterfacetestingapp.components.CheckboxWithLabel
import com.example.demokmpinterfacetestingapp.components.PickImageButton
import com.example.demokmpinterfacetestingapp.ui.showToast
import com.example.demokmpinterfacetestingapp.util.decodeImage

private val viewModel: AppWizardViewModel = wizardViewModel
@Composable
fun AppCreationScreen() {
    val uiState by viewModel.uiState.collectAsState()

    val pages = listOf<@Composable () -> Unit>(
        { ChooseName() },
        { ChooseModules() },
        { ChooseColor() },
        { chooseAppImage() }

        // Add more steps as needed

    )

    WizardScreen(pages = pages,
        onFinish = {
            viewModel.sendDataToServer()
            navRouter.navigate(Screen.AppSelectionScreen)

        }
        )

    //Send to the server the info to create the app
    //recreate loacally the app & store it in local db
    //go to the app main screen

}


@Composable
fun ChooseName() {
    val uiState by viewModel.uiState.collectAsState()
    var appName = remember { mutableStateOf("") }

    Text("What is your app's name?")
    TextField(
        value = uiState.appName,
        onValueChange = {
            viewModel.onNameChange(it)
                        },
        placeholder = { Text("Enter your new app name") }
    )
}

@Composable
fun ChooseModules() {
    val uiState by viewModel.uiState.collectAsState()
    Text("What modules are you planning to use?")
    CheckboxWithLabel(Module.Calendar.name){ viewModel.toggleList(Module.Calendar) }
    CheckboxWithLabel(Module.Contacts.name){ viewModel.toggleList(Module.Contacts) }
    CheckboxWithLabel(Module.Tasks.name){ viewModel.toggleList(Module.Tasks) }
    viewModel.enableNext(uiState.toggledList.isNotEmpty())
}

@Composable
fun ChooseColor() {
    val uiState by viewModel.uiState.collectAsState()
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    Text("What color theme do you prefer?")
    ColorSelector(selectedColor = uiState.selectedColor, onColorSelected = { viewModel.selectColor(it) })

    Spacer(modifier = Modifier.height(12.dp))

    uiState.selectedColor?.let {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Selected:")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(it, RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            )
        }
    }
}


@Composable
fun chooseAppImage() {
    val uiState = viewModel.uiState.collectAsState()
    viewModel.enableNext(true)
    Column(Modifier.padding(16.dp)) {
    Text("Choose an App Image (optional)")

        PickImageButton { image -> viewModel.setPickedImage(image) }



        Spacer(Modifier.height(12.dp))
       uiState.value.pickedImage?.let { image ->
           //convert to bitmap and display
            val bmp = remember(image) { decodeImage(image.bytes) }
            Image(bitmap = bmp, contentDescription = "Selected image", modifier = Modifier.size(120.dp))
            Text("Selected Image for app ${viewModel.uiState.value.appName}")

        }
        }
    }






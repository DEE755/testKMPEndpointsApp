package com.example.demokmpinterfacetestingapp.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.components.ColorSelector
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.wizardViewModel
import com.example.demokmpinterfacetestingapp.Model.models.Module.Module
import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import com.example.demokmpinterfacetestingapp.navigation.Screen
import com.example.demokmpinterfacetestingapp.viewmodel.AppWizardViewModel
import com.example.demokmpinterfacetestingapp.components.CheckboxWithLabel
import com.example.demokmpinterfacetestingapp.components.ModuleRecyclerScreen
import com.example.demokmpinterfacetestingapp.components.PickImage
import com.example.demokmpinterfacetestingapp.components.PickImageButton
import com.example.demokmpinterfacetestingapp.components.PickImageRectangleButton
import com.example.demokmpinterfacetestingapp.components.RecyclerScreen
import com.example.demokmpinterfacetestingapp.components.VectorialPlusButton
import com.example.demokmpinterfacetestingapp.components.radialVignette
import com.example.demokmpinterfacetestingapp.util.decodeImage

private val viewModel: AppWizardViewModel = wizardViewModel
@Composable
fun AppCreationScreen() {
    val uiState by viewModel.uiState.collectAsState()

    val showModulesSelection = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        var showLogoPicker by remember { mutableStateOf(false) }
        var showBannerPicker by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RectangleShape)
                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        TextField(
                            value = uiState.appName,
                            onValueChange = viewModel::onNameChange,
                            placeholder = { Text("Add App Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        uiState.pickedImageBanner?.let { banner ->
                            val bannerBitmap = remember(banner) { decodeImage(banner.bytes) }
                            Image(
                                bitmap = bannerBitmap,
                                contentDescription = "App banner",
                                modifier = Modifier
                                    .height(150.dp)
                                    .clip(RectangleShape)
                                    .border(1.dp, Color.Gray, RectangleShape)
                                    .weight(1f)
                                    .clickable { showBannerPicker = true },
                                contentScale = ContentScale.Crop
                            )

                            if (showBannerPicker) {
                                PickImage { image ->
                                    viewModel.setPickedBannerImage(image)
                                    showBannerPicker = false
                                }
                            }
                        } ?: Box(
                            modifier = Modifier
                                .height(150.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Box {
                                Column {  //Text("Add Banner")
                                PickImageRectangleButton(shape = RectangleShape) { image ->
                                    viewModel.setPickedBannerImage(image)
                                }
                                    }
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
                    }
            }

            uiState.pickedImageLogo?.let { logo ->
                val logoBitmap = remember(logo) { decodeImage(logo.bytes) }
                Image(
                    bitmap = logoBitmap,
                    contentDescription = "App logo",
                    modifier = Modifier
                        .size(210.dp)
                        .offset(x = 12.dp, y = (-10).dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .radialVignette(edgeColor = Color.Black.copy(alpha = 0.3f), radius = 0.9f)
                        .align(Alignment.TopEnd)
                        .clickable { showLogoPicker = true },
                    contentScale = ContentScale.Crop
                )
                Image(
                    bitmap = logoBitmap,
                    contentDescription = "App logo small",
                    modifier = Modifier
                        .size(35.dp)
                        .offset(x = (-5).dp, y = (10).dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .radialVignette(edgeColor = Color.Black.copy(alpha = 0.3f), radius = 0.9f)
                        .align(Alignment.BottomStart),
                    contentScale = ContentScale.Crop
                )

                if (showLogoPicker) {
                    PickImage { image ->
                        viewModel.setPickedImage(image)
                        showLogoPicker = false
                    }
                }
            } ?: Box(
                modifier = Modifier
                    .size(110.dp)
                    .offset(x = 12.dp, y = (-10).dp)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Add Logo")
                    Spacer(Modifier.height(4.dp))
                    PickImageButton { image -> viewModel.setPickedImage(image) }
                }
            }
        }

        Spacer(Modifier.height(24.dp))



        if (uiState.toggledList.isEmpty() && !showModulesSelection.value) {
            Spacer(Modifier.height(300.dp))
            Text("Your app does nothing, please click the + button to add components")


        } else if (uiState.toggledList.isNotEmpty() && !showModulesSelection.value) {
            val modules = uiState.toggledList
            val itemsList = mutableListOf<ListItem>()
            var index = 0
            for (module in modules) {
                ++index
                val item = ListItem(id = index.toString(), title = "App Module : ${module.name}", "", bannerUrl = "")
                itemsList.add(item)
            }
            RecyclerScreen(itemsList, modifier = Modifier.weight(0.7f))
        }


            if (showModulesSelection.value) {
                val modules = Module.entries
                val itemsList = mutableListOf<ListItem>()
                var index = 0
                for (module in modules) {
                    ++index
                    val item = ListItem(id = module.name, title = module.name, "", bannerUrl = "")
                    itemsList.add(item)
                }

                Column {

                    Row(horizontalArrangement = Arrangement.End){
                    Button(onClick = {
                        showModulesSelection.value = false
                    }) {
                        Text("Done")
                    }
                        }

                    ModuleRecyclerScreen(
                        itemsList,
                        modifier = Modifier.weight(1f),
                        selectedIds = uiState.toggledList.map { it.name }.toSet(),
                        onCheckedChange = { moduleName, isChecked ->
                            val module = Module.valueOf(moduleName)
                            val currentlySelected = uiState.toggledList.contains(module)
                            if (isChecked != currentlySelected) {
                                viewModel.toggleList(module)
                            }
                        },
                        onItemClick = {
                            val module = Module.valueOf(it.title)
                            viewModel.toggleList(module)
                        }
                    )

                }
            } else {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                VectorialPlusButton(onClick = { showModulesSelection.value = true })
            }
        }


        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                navRouter.navigate(Screen.AppSelectionScreen)
            }) {
                Text("Cancel")
            }

            Button(onClick = {
                viewModel.sendDataToServer()
                navRouter.navigate(Screen.AppSelectionScreen)
                viewModel.close()
            },
                enabled = uiState.appName.isNotEmpty(),
            ) {
                Text("Save")
            }

        }



    }




    @Composable
    fun ChooseName() {
        val uiState by viewModel.uiState.collectAsState()
        val appName = remember { mutableStateOf("") }

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
        CheckboxWithLabel(Module.Calendar.name) { viewModel.toggleList(Module.Calendar) }
        CheckboxWithLabel(Module.Contacts.name) { viewModel.toggleList(Module.Contacts) }
        CheckboxWithLabel(Module.Tasks.name) { viewModel.toggleList(Module.Tasks) }
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
            uiState.value.pickedImageLogo?.let { image ->
                //convert to bitmap and display
                val bmp = remember(image) { decodeImage(image.bytes) }
                Image(bitmap = bmp, contentDescription = "Selected image", modifier = Modifier.size(120.dp))
                Text("Selected Image for app ${viewModel.uiState.value.appName}")

            }
        }
    }

}

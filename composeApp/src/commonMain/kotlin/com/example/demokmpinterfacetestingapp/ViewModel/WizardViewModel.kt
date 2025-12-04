package com.example.demokmpinterfacetestingapp.ViewModel

import androidx.compose.ui.graphics.Color
import com.example.demokmpinterfacetestingapp.Model.models.Module.Module
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WizardViewModel {

//NOTE: USING GOOGLE CONVENTION FOR NAMING VARIABLES IN DATA CLASS AND OBSERVE IN READONLY WAY

//We do that to be able to observe all the screen states from a single data class
data class WizardUiState(
    val nextEnabled: Boolean = false,
    val appName: String = "",
    var selectedColor: Color? = null,
    val toggledList: List<Module> = emptyList()
)

    private val _uiState = MutableStateFlow(WizardUiState())
    val uiState: StateFlow<WizardUiState> = _uiState

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            appName = newName,
            nextEnabled = newName.isNotBlank()
        )
    }

    fun sendDataToServer() {

        println("Sending data to server: App Name - ${uiState.value.appName}, Selected Color - ${uiState.value.selectedColor}, " +
        "Selected Modules - ${uiState.value.toggledList.joinToString { it.toString() }}")
        //repository.sendAppCreationData(appName, selectedColor)
    }


    fun enableNext(state: Boolean) {
        _uiState.value = _uiState.value.copy(nextEnabled = state)
    }


    fun toggleList(module: Module) {
        val currentList = _uiState.value.toggledList.toMutableList()
        if (currentList.contains(module)) {
            currentList.remove(module)
        } else {
            currentList.add(module)
        }
        _uiState.value = _uiState.value.copy(toggledList = currentList)
        enableNext(currentList.isNotEmpty())
    }

    fun selectColor(color: Color) {
        _uiState.value = _uiState.value.copy(selectedColor = color)
        enableNext(true)
    }

}
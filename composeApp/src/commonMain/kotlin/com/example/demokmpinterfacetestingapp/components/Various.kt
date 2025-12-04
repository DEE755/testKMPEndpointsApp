package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.wizardViewModel
import com.example.demokmpinterfacetestingapp.ViewModel.WizardViewModel

val viewModel: WizardViewModel = wizardViewModel

@Composable
fun CheckboxWithLabel(label: String, onCheckedChange: () -> Unit = {}) {
    val isChecked = remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = it
                onCheckedChange.invoke()
            }
        )
        Text(label)
    }
}




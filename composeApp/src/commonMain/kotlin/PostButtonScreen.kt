package com.example.demokmpinterfacetestingapp

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PostButtonScreen(repository: NetworkRepository) {
    var phone by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number to test") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                loading = true
                result = ""
                scope.launch {
                    try {
                        result = repository.sendMobilePhone(phone)
                    } catch (e: Exception) {
                        result = "Erreur: ${e.message}"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading && phone.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Envoyer")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (result.isNotBlank()) {
            Text(result)
        }
    }
}

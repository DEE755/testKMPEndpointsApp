package com.example.demokmpinterfacetestingapp.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import di.ServiceLocator.authRepository
import di.ServiceLocator.userRepository

@Composable
fun PromptFromUserSeriesScreen(
    navRouter: Router? = null,
    questionsList: List<String>,
    viewModel: LogInOutViewModel? = null,

    functionToExecute: () -> Unit = {}
) {
    val viewModel = viewModel ?: remember { LogInOutViewModel(authRepository, userRepository) }
    val router = navRouter ?: remember { Router() }
    val uiState by viewModel.uiState.collectAsState()

    val answers = remember { mutableStateListOf<String>() }
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    var currentAnswer by rememberSaveable { mutableStateOf("") }
    val executed = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)) {
        Spacer(modifier = Modifier.padding(28.dp))
        if (currentIndex >= questionsList.size) {
            Text("Answers: ${answers.joinToString()}")
            LaunchedEffect(currentIndex, answers.size) {
                if (!executed.value) {
                    functionToExecute()
                    executed.value = true
                }
            }

            return@Column
        }

        Text(text = questionsList.getOrNull(currentIndex) ?: "")

        TextField(
            value = uiState.temporaryUsername,
            onValueChange = viewModel::onAnswerChange,
            label = { Text("Your answer") }
        )

        Row {
            Button(onClick = {
                if (currentAnswer.isNotBlank() || uiState.temporaryUsername.isNotBlank()) {//change later
                    currentAnswer = uiState.temporaryUsername //change late
                    if (answers.size > currentIndex) answers[currentIndex] = currentAnswer
                    else answers.add(currentAnswer)
                    currentAnswer = ""
                    currentIndex++
                }
            }) {
                Text(if (currentIndex >= questionsList.size - 1) "Finish" else "Continue")
            }

            Button(onClick = { }) {
                Text("Back")
            }
        }
    }
}
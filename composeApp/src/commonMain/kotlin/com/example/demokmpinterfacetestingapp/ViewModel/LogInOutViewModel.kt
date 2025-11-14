package com.example.demokmpinterfacetestingapp.ViewModel

import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInOutViewModel(//injecting common interface
    val authRepository : AuthRepository,
    val userRepository: UserRepository,
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    val questionList1: List<String> = listOf("Hello", "Hi", "How are you?", "What is your name?", "Where are you from?")
    val appCreationList: List<String> = listOf("What is your App Name", "What is your App Description")
    data class LoginUiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val validPassword: Boolean = false,
        val phoneNumber: String = "",
        val currentUser: User? = null,
        val validEmail: Boolean = false,
        val emailConfirmation: String = "",
        val temporaryUsername: String = ""
    )

    data class ConnectionStatus(
        val isConnected: Boolean = false,
        val error: Exception? = null
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _connectionStatus = MutableStateFlow(ConnectionStatus())
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus



    fun onEmailChange(newEmail: String): Unit {
        _uiState.value = _uiState.value.copy(email = newEmail)

        if (newEmail.length < 3 || newEmail.contains("@", ).not() || newEmail.contains(".", ).not())
            _uiState.value = _uiState.value.copy(validEmail = false)
        else
            _uiState.value = _uiState.value.copy(validEmail = true)


    }

    fun onEmailConfirmationChange(newEmailConfirmation: String): Unit {
        _uiState.value = _uiState.value.copy(emailConfirmation = newEmailConfirmation)
    }

    fun onPasswordChange(newPassword: String) {
        val specialChar = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\"
        _uiState.value = _uiState.value.copy(password = newPassword)

        if (newPassword.length < 8  || newPassword.any { it.isDigit() }.not() || newPassword.any { it in specialChar }.not())
            _uiState.value = _uiState.value.copy(validPassword = false)
        else
            _uiState.value = _uiState.value.copy(validPassword = true)

    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newPhoneNumber)

    }


    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(temporaryUsername = newUsername)

    }

    //TODO(ADAPT FOR MAPPING QUESTIONS->ANSWER-->PLACE ANSWERS IN CORRECT FIELD, called in onclick )
    fun onAnswerChange(newAnswer: String, selectedQuestionSet: Int =0) {
        if (selectedQuestionSet == 0)
        _uiState.value = _uiState.value.copy(temporaryUsername = newAnswer)
    }

    fun onValidateUsername() {


            //updating the username remotely
            viewModelScope.launch {
                userRepository.UpdateUsername(_uiState.value.temporaryUsername, _uiState.value.currentUser)
            }

            //updating locally too
            val currentUser = _uiState.value.currentUser
            currentUser?.username = _uiState.value.temporaryUsername

            _uiState.value = _uiState.value.copy(currentUser = currentUser)

    }

    fun onSMSSignUpClick() = viewModelScope.launch { authRepository.sendMobilePhone(_uiState.value.phoneNumber) }

    fun emailSignIn() {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {


                val receivedUser = authRepository.emailSignIn(
                    _uiState.value.email,
                    _uiState.value.password
                )

                if (receivedUser != null) {
                    _uiState.value = _uiState.value.copy(currentUser = receivedUser)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = true, error = null)
            }

            catch (e: Exception) {
                _uiState.value.copy(isLoading = false)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = e)

            }
        }

    }



    fun emailSignUp() {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {

                authRepository.emailSignUp(
                    _uiState.value.email,
                    _uiState.value.password,
                    _uiState.value.temporaryUsername
                )

                _uiState.value = _uiState.value.copy(isLoading = false)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = true, error = null)
            }

            catch (e: Exception) {
                _uiState.value.copy(isLoading = false)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = e)

            }
        }

    }


    fun setConnected(connected: Boolean) {
        _connectionStatus.value = _connectionStatus.value.copy(isConnected = connected)
    }

    fun signOut() {
        viewModelScope.launch {
            //com.example.demokmpinterfacetestingapp.repository.signOut()
            _uiState.value = _uiState.value.copy(currentUser = null, password = "")
            _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = null)

        }

    }

}
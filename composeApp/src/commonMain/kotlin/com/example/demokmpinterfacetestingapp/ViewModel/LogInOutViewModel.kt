package com.example.demokmpinterfacetestingapp.ViewModel

import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.AuthTokenProvider
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.ui.showToast
import com.example.demokmpinterfacetestingapp.util.PickedImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.io.println

class LogInOutViewModel(
    val authRepository : AuthRepository,
    val userRepository: UserRepository,
    val cloudFilesRepository: CloudFilesRepository? = null,
    private val tokenProvider: AuthTokenProvider,
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    val signUpQuestionsList: List<String> = listOf("Hello", "Hi", "How are you?", "What is your name?", "Where are you from?")
    val appCreationList: List<String> = listOf("What is your App Name", "What is your App Description")

    val signUpQuestionsAnswersMap: MutableMap<String, String> = mutableMapOf("Choose an username" to "")

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

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
        _uiState.value = _uiState.value.copy(
            validEmail = newEmail.length >= 3 && newEmail.contains("@") && newEmail.contains(".")
        )
    }

    fun onEmailConfirmationChange(newEmailConfirmation: String) {
        _uiState.value = _uiState.value.copy(emailConfirmation = newEmailConfirmation)
    }

    fun onPasswordChange(newPassword: String) {
        val specialChar = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\"
        _uiState.value = _uiState.value.copy(password = newPassword)
        _uiState.value = _uiState.value.copy(
            validPassword = newPassword.length >= 8 &&
                    newPassword.any { it.isDigit() } &&
                    newPassword.any { it in specialChar }
        )
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newPhoneNumber)
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(temporaryUsername = newUsername)
    }

    fun onAnswerChange(newAnswer: String, selectedQuestionSet: Int = 0) {
        if (selectedQuestionSet == 0)
            _uiState.value = _uiState.value.copy(temporaryUsername = newAnswer)
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = e)
            }
        }
    }

    fun setUsername(username: String) {
        _uiState.value = _uiState.value.copy(
            temporaryUsername = username
        )
    }

    fun setUser(user: User?) {
        _uiState.value = _uiState.value.copy(currentUser = user)
    }

    suspend fun emailSignUp() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val createdUser = authRepository.emailSignUp(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.temporaryUsername
            )

            if (createdUser != null) {
                _uiState.value = _uiState.value.copy(currentUser = createdUser)
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = true, error = null)
            }
            _uiState.value = _uiState.value.copy(isLoading = false)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false)
            _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = e)
        }
    }

    fun setConnected(connected: Boolean) {
        _connectionStatus.value = _connectionStatus.value.copy(isConnected = connected)
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(currentUser = null, password = "")
            _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = null)
        }
    }

    fun presignAppFileUpload(fileName: String, fileType: String, appId: String) =
        viewModelScope.launch {
            try {
                cloudFilesRepository?.presignAppFileUpload(
                    folder = "apps/$appId/files/",
                    file_basename = fileName,
                    mime = fileType,
                    ext = fileType.substringAfterLast("/")
                )
            } catch (e: Exception) {
                _connectionStatus.value = _connectionStatus.value.copy(error = e)
            }
        }

    fun presignUserFileUpload(fileName: String, fileType: String) =
        viewModelScope.launch {
            try {
                cloudFilesRepository?.presignUserFileUpload(
                    owner_id = _uiState.value.currentUser?._id ?: "",
                    folder = "users/${_uiState.value.currentUser?._id}/files/",
                    file_basename = fileName,
                    mime = fileType,
                    ext = fileType.substringAfterLast("/")
                )
            } catch (e: Exception) {
                _connectionStatus.value = _connectionStatus.value.copy(error = e)
            }
        }

    fun commitAppFile(key: String, tags: List<String> = emptyList(), checksum: String? = null) =
        viewModelScope.launch {
            try {
                cloudFilesRepository?.commitAppFile(key, tags, checksum)
            } catch (e: Exception) {
                _connectionStatus.value = _connectionStatus.value.copy(error = e)
            }
        }

    fun uploadUserImage(image: PickedImage, folder: String, fileBasename: String) {
        viewModelScope.launch {
            try {
                if (_uiState.value.currentUser == null || _uiState.value.currentUser?._id == null) {
                    throw Exception("No current user found for user file upload")
                }
                val presignResponse = cloudFilesRepository?.presignUserFileUpload(
                    owner_id = _uiState.value.currentUser!!._id,
                    folder = folder,
                    file_basename = fileBasename,
                    mime = image.mimeType ?: "image/jpeg",
                    ext = image.name?.substringAfterLast('.') ?: "jpg",
                    visibility = Visibility.PUBLIC
                )

                if (presignResponse == null) {
                    throw Exception("Failed to obtain presigned URL")
                }

                val uploadSuccess = cloudFilesRepository.uploadToR2(
                    presignedUrl = presignResponse.url,
                    fileBytes = image.bytes,
                    contentType = image.mimeType ?: "image/jpeg"
                )

                if (uploadSuccess) {
                    cloudFilesRepository.commitUserFile(
                        owner_id = _uiState.value.currentUser!!._id,
                        key = presignResponse.key,
                        tags = listOf("app-image"),
                        checksum = null,
                    )
                    println("Upload successful: ${image.name} of type : ${image.mimeType}, Answer: ${presignResponse.key}")
                    showToast("Upload successful: ${image.name}")
                }
            } catch (e: Exception) {
                println("Upload failed: ${e.message}")
                showToast("Upload failed: ${image.name}")
            }
        }
    }

    fun uploadAppImage(image: PickedImage, folder: String, fileBasename: String) {
        viewModelScope.launch {
            try {
                val presignResponse = cloudFilesRepository?.presignAppFileUpload(
                    folder = folder,
                    file_basename = fileBasename,
                    mime = image.mimeType ?: "image/jpeg",
                    ext = image.name?.substringAfterLast('.') ?: "jpg",
                    visibility = Visibility.PUBLIC
                )

                if (presignResponse == null) {
                    throw Exception("Failed to obtain presigned URL")
                }

                val uploadSuccess = cloudFilesRepository.uploadToR2(
                    presignedUrl = presignResponse.url,
                    fileBytes = image.bytes,
                    contentType = image.mimeType ?: "image/jpeg"
                )

                if (uploadSuccess) {
                    cloudFilesRepository.commitAppFile(
                        key = presignResponse.key,
                        tags = listOf("app-image"),
                        checksum = null
                    )
                    println("Upload successful: ${image.name} of type : ${image.mimeType}, Answer: ${presignResponse.key}")
                    showToast("Upload successful: ${image.name}")
                }
            } catch (e: Exception) {
                println("Upload failed: ${e.message}")
                showToast("Upload failed: ${image.name}")
            }
        }
    }

    fun getFilesListFromCloudDB(folder: String) = viewModelScope.launch {
        try {
            cloudFilesRepository?.getFilesListFromCloudDB(folder)
        } catch (e: Exception) {
            _connectionStatus.value = _connectionStatus.value.copy(error = e)
        }
    }

    fun saveAccessToken(token: String) { //replace by saving access token already when receiving response without calling back from viewmodel
        viewModelScope.launch {
           authRepository.saveAccessToken(token)
        }
    }



    fun googleSignIn(idToken: String, nonce: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val receivedUser = authRepository.googleSignIn(idToken, nonce)
                if (receivedUser != null) {
                    _uiState.value = _uiState.value.copy(currentUser = receivedUser)
                    _connectionStatus.value = _connectionStatus.value.copy(isConnected = true, error = null)
                }
            } catch (e: Exception) {
                _connectionStatus.value = _connectionStatus.value.copy(isConnected = false, error = e)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }


    fun tryAndGetUserFromToken() {
        if (!tokenProvider.hasBearerSet) return
        viewModelScope.launch {
            try {
                val fetchedUser: User? = authRepository.getCurrentUser()
                fetchedUser?.let {
                    setUser(it)
                    setConnected(true)
                }
            } catch (e: Exception) {
                println("failed to get user from token: ${e.message}")
            }
        }
    }

    suspend fun logout() {
        tokenProvider.clearAccessToken()
        tokenProvider.hasBearerSet = false
        setUser(null)
        setConnected(false)
    }
}

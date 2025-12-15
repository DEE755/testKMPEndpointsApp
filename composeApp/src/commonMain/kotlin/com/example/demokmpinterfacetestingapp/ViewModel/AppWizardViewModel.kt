package com.example.demokmpinterfacetestingapp.ViewModel

import androidx.compose.ui.graphics.Color

import com.example.demokmpinterfacetestingapp.Model.models.Module.Module
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.Repository.AppRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.UserLocalDataSource
import com.example.demokmpinterfacetestingapp.SessionManager
import com.example.demokmpinterfacetestingapp.ui.showToast
import com.example.demokmpinterfacetestingapp.util.PickedImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime



class AppWizardViewModel(
    private val appRepository: AppRepository,
    private val cloudFilesRepository: CloudFilesRepository,
    private val sessionManager: SessionManager,
    private val localDataSource: UserLocalDataSource // TODO (fustionate sessionManager and localDataSource)
) {

//NOTE: USING GOOGLE CONVENTION FOR NAMING VARIABLES IN DATA CLASS AND OBSERVE IN READONLY WAY

//We do that to be able to observe all the screen states from a single data class
    //TODO(Later pass GlobalUserRepo and userPrefs will be included inside it )
data class WizardUiState(
    val nextEnabled: Boolean = false,
    val appName: String = "",
    var selectedColor: Color= Color.White,
    val toggledList: List<Module> = emptyList(),
    val pickedImage: PickedImage? = null,
    val currentUser : User? = null
)

    private val _uiState = MutableStateFlow(WizardUiState())
    val uiState: StateFlow<WizardUiState> = _uiState


    init {
        runBlocking {
            val user = sessionManager.getCachedUser()
            _uiState.value = _uiState.value.copy(currentUser = user)
        }
    }



    private val viewModelScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }



    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            appName = newName,
            nextEnabled = newName.isNotBlank()
        )
    }

    fun sendDataToServer() {

        viewModelScope.launch(Dispatchers.IO) {
            appRepository.setLoading(true)
            try {
                val logoKey = uiState.value.pickedImage
                    ?.let { uploadNewAppLogo(it, "app_icons") }
                    ?: ""

                val createdApp=appRepository.createApp(
                    uiState.value.appName,
                    uiState.value.toggledList,
                    uiState.value.selectedColor,
                    logoKey
                )

                createdApp.let{
                    appRepository.setLoading(false)
                    showToast("Server: App ${it.name} created successfully!")
                }
            } catch (e: Exception) {
                println(e)
                showToast(e.message.orEmpty())
                appRepository.setLoading(false)
            }
        }
    }


    @OptIn(ExperimentalTime::class)
    suspend fun uploadNewAppLogo(image: PickedImage, folder: String): String {
        if(uiState.value.currentUser == null){
            throw Exception("User not logged in")
        }
        val timeStamp = Clock.System.now().toEpochMilliseconds()
        val fileBasename = "${uiState.value.appName}_app_icon_$timeStamp"


        val presign = cloudFilesRepository.presignUserFileUpload(
            owner_id = uiState.value.currentUser!!._id,
            folder = folder,
            file_basename = fileBasename,
            mime = image.mimeType ?: "image/jpeg",
            ext = image.name?.substringAfterLast('.') ?: "jpg",
            visibility = Visibility.public
        )

        cloudFilesRepository.uploadToR2(
            presignedUrl = presign.url,
            fileBytes = image.bytes,
            contentType = image.mimeType ?: "image/jpeg"
        )

        return cloudFilesRepository.commitUserFile(
            owner_id = uiState.value.currentUser!!._id, //can't be null here
            key = presign.key,
            tags = listOf("app-image"),
            checksum = null
        ).key
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


    fun setPickedImage(pickedImage: PickedImage) {
        _uiState.value = _uiState.value.copy(pickedImage = pickedImage)
        enableNext(true)
    }


}
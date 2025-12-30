package com.example.demokmpinterfacetestingapp.viewmodel

import androidx.compose.ui.graphics.Color

import com.example.demokmpinterfacetestingapp.Model.models.Module.Module
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.repository.AppRepository
import com.example.demokmpinterfacetestingapp.repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.SessionManager
import com.example.demokmpinterfacetestingapp.ui.showToast
import com.example.demokmpinterfacetestingapp.util.PickedImage
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime



class AppWizardViewModel (
    private val appRepository: AppRepository,
    private val cloudFilesRepository: CloudFilesRepository,
    private val sessionManager: SessionManager
) : Closeable {

//NOTE: USING GOOGLE CONVENTION FOR NAMING VARIABLES IN DATA CLASS AND OBSERVE IN READONLY WAY

//We do that to be able to observe all the screen states from a single data class
data class WizardUiState(
    val nextEnabled: Boolean = false,
    val appName: String = "",
    var selectedColor: Color= Color.White,
    val toggledList: List<Module> = emptyList(),
    val pickedImageLogo: PickedImage? = null,
    val pickedImageBanner: PickedImage? = null,
    val currentUser : User? = null
)

    private val _uiState = MutableStateFlow(WizardUiState())
    val uiState: StateFlow<WizardUiState> = _uiState


    init {
        runBlocking {
            val user = sessionManager.getCachedUser()
            _uiState.value = _uiState.value.copy(currentUser = user)
            println("current user: ${uiState.value.currentUser}")
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
                val logoKey = uiState.value.pickedImageLogo
                    ?.let {logo->
                        println("updating NEW image: $logo")
                        uploadNewAppImage(logo, "app_icons")

                    }
                    ?: ""

                val bannerKey = uiState.value.pickedImageBanner
                    ?.let {banner->
                        println("updating NEW banner image: $banner")

                        uploadNewAppImage(banner, "app_banners")

                    }
                    ?: ""

                val createdApp=appRepository.createApp(
                    uiState.value.appName,
                    uiState.value.toggledList,
                    uiState.value.selectedColor,
                    logoKey,
                    bannerKey
                )

                // Refresh from server so we get the app_icon_url and app_banner_url updated
                try {
                    appRepository.fetchUserApps()
                } catch (refreshErr: Exception) {
                    println("Warning: failed to refresh apps after creation: $refreshErr")
                }

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
    suspend fun uploadNewAppImage(image: PickedImage, folder: String): String {
        if(uiState.value.currentUser == null){
            throw Exception("User not logged in")
        }
        val timeStamp = Clock.System.now().toEpochMilliseconds()
        val fileBasename = "${uiState.value.appName}_${folder}_$timeStamp"


        val presign = cloudFilesRepository.presignUserFileUpload(
            ownerId = uiState.value.currentUser!!._id,
            folder = folder,
            fileBasename = fileBasename,
            mime = image.mimeType ?: "image/jpeg",
            ext = image.name?.substringAfterLast('.') ?: "jpg",
            visibility = Visibility.public
        )

        println("uploading to R2: $presign")
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
        _uiState.value = _uiState.value.copy(pickedImageLogo = pickedImage)
        enableNext(true)
    }


    fun setPickedBannerImage(pickedImage: PickedImage) {
        _uiState.value = _uiState.value.copy(pickedImageBanner = pickedImage)
        enableNext(true)
    }

    override fun close() {
        //viewModelScope.cancel()
        //_uiState.value = WizardUiState()
    }

}

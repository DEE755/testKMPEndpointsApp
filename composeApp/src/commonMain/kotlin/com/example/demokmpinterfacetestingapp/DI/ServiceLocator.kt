
package com.example.demokmpinterfacetestingapp.DI

import androidx.compose.ui.graphics.CloseSegment
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.UserCloudDataSource
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.Repository.AppRepository
import com.example.demokmpinterfacetestingapp.Repository.GlobalUserRepository
import com.example.demokmpinterfacetestingapp.Repository.UserLocalDataSource
import com.example.demokmpinterfacetestingapp.ViewModel.AppSelectionViewModel

import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.HttpHeaders.Authorization
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.example.demokmpinterfacetestingapp.SessionManager
import com.example.demokmpinterfacetestingapp.ViewModel.AppWizardViewModel

// 1) Fonctions "ports" à spécialiser par plateforme
expect fun provideEngine(): HttpClientEngineFactory<*>
expect fun provideAuthRepository(client: HttpClient): AuthRepository

expect fun provideUserRepository(client: HttpClient, userPrefs1: UserPrefsDataSource): UserCloudDataSource

expect fun provideAppRepository(client: HttpClient): AppRepository


expect fun provideCloudFilesRepository(clientWithBearer : HttpClient, cleanClient : HttpClient): CloudFilesRepository

expect fun provideLogger(): Logger

expect fun provideAppContextInstance(): Any

expect fun provideTokenProvider(): AuthTokenProvider

expect fun provideUserPrefsDataSource(): UserPrefsDataSource

expect fun provideUserLocalDataSource(): UserLocalDataSource

// 2) ServiceLocator commun (pas expect) qui compose tout
object ServiceLocator {

    //Global Repositories


    fun provideSessionManager(): SessionManager {
        return SessionManager(
            tokenProvider = tokenProvider,
            authRepository = authRepository
        )
    }
    fun provideGlobalUserRepository(): GlobalUserRepository {
        return GlobalUserRepository(
            cloud = userCloudRepository,
            local = localUserDataSource,
            appRepository = appRepository

        )
    }



//viewModels
    fun provideLogInOutViewModel(): LogInOutViewModel {
        return LogInOutViewModel(
            authRepository = authRepository,
            userRepository = userCloudRepository,
            sessionManager = sessionManager,
        )
    }

    fun provideAppSelectionViewModel(): AppSelectionViewModel {
        return AppSelectionViewModel(
            userGlobalRepository, sessionManager
        )
    }

    fun provideWizardViewModel(): AppWizardViewModel {
        return AppWizardViewModel(appRepository, cloudFilesRepository, sessionManager, localUserDataSource)
    }
    fun provideNavRouter() : Router {
        return Router()
    }






    val httpClient: HttpClient by lazy {
        HttpClient(provideEngine()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = false
                        explicitNulls = false

                    }
                )
            }

            // Add Authorization "Bearer <token>" if available
            install(DefaultRequest) {

               runBlocking { //reading token is a suspend function but this block is not suspend, so we use runBlocking, it is ok here because this is called only once at initialization
                   val token = tokenProvider.getAccessToken()
                   if (!token.isNullOrBlank()) {

                       headers.append(Authorization, "Bearer $token")
                       tokenProvider.hasBearerSet = true
                   }
               }
            }


            install(Logging) {
                logger = provideLogger()
                level = LogLevel.ALL
            }
        }
    }


    val cleanHttpClient: HttpClient by lazy {
        HttpClient(provideEngine()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = false
                        explicitNulls = false
                    }
                )
            }

            install(Logging) {
                logger = provideLogger()
                level = LogLevel.ALL
            }
        }
    }


    // Repo fourni par la plateforme, mais construit ici avec le client commun
    val authRepository: AuthRepository by lazy {
        provideAuthRepository(httpClient)
    }

    val userCloudRepository: UserCloudDataSource by lazy {
        provideUserRepository(httpClient, userPrefs)
    }

    val userGlobalRepository: GlobalUserRepository by lazy {
        provideGlobalUserRepository()
    }

    val appRepository : AppRepository by lazy {
        provideAppRepository(httpClient)
    }

    val cloudFilesRepository by lazy {
        provideCloudFilesRepository(httpClient, cleanHttpClient)
    }

    val sessionManager by lazy {
        provideSessionManager()

    }


    val appContextInstance by lazy {
        provideAppContextInstance()
    }


    val tokenProvider: AuthTokenProvider by lazy {
        provideTokenProvider()
    }


    val logInOutViewModel: LogInOutViewModel by lazy {
        provideLogInOutViewModel()
    }


    val wizardViewModel: AppWizardViewModel by lazy {
        provideWizardViewModel()
    }

    val appSelectionViewModel : AppSelectionViewModel by lazy {
        provideAppSelectionViewModel()
    }

    val navRouter : Router by lazy {
        provideNavRouter()
    }

    val userPrefs : UserPrefsDataSource by lazy {
        provideUserPrefsDataSource()
    }

    val localUserDataSource :  UserLocalDataSource by lazy {
        provideUserLocalDataSource()
    }


}
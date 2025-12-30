
package com.example.demokmpinterfacetestingapp.DI

import com.example.demokmpinterfacetestingapp.repository.AuthRepository
import com.example.demokmpinterfacetestingapp.repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.repository.UserCloudDataSource
import com.example.demokmpinterfacetestingapp.viewmodel.AuthViewModel
import com.example.demokmpinterfacetestingapp.navigation.Router
import com.example.demokmpinterfacetestingapp.repository.AppRemoteDataSource
import com.example.demokmpinterfacetestingapp.repository.AppRepository
import com.example.demokmpinterfacetestingapp.repository.CommonAppRepository
import com.example.demokmpinterfacetestingapp.repository.GlobalUserRepository
import com.example.demokmpinterfacetestingapp.repository.UserLocalDataSource
import com.example.demokmpinterfacetestingapp.viewmodel.AppSelectionViewModel

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
import com.example.demokmpinterfacetestingapp.viewmodel.AppWizardViewModel

// 1) Fonctions "ports" à spécialiser par plateforme
expect fun provideEngine(): HttpClientEngineFactory<*>
expect fun provideAuthRepository(client: HttpClient): AuthRepository

expect fun provideUserRepository(client: HttpClient, userPrefs1: UserPrefsDataSource): UserCloudDataSource

expect fun provideAppRemoteDataSource(client: HttpClient): AppRemoteDataSource



expect fun provideCloudFilesRepository(clientWithBearer : HttpClient, cleanClient : HttpClient): CloudFilesRepository

expect fun provideLogger(): Logger

expect fun provideAppContextInstance(): Any

expect fun provideTokenProvider(): AuthTokenProvider

expect fun provideUserPrefsDataSource(): UserPrefsDataSource

expect fun provideUserLocalDataSource(): UserLocalDataSource

// 2) ServiceLocator commun (pas expect) qui compose tout
object ServiceLocator {

    //Global Repositories


    fun provideAppRepository(): AppRepository {
        return CommonAppRepository(
           appRemoteDataSource,
        )
    }

    fun provideSessionManager(): SessionManager {
        return SessionManager(
            tokenProvider = tokenProvider,
            authRepository = authRepository,
            userPrefsDataSource = userPrefs
        )
    }
    fun provideGlobalUserRepository(): GlobalUserRepository {
        return GlobalUserRepository(
            cloud = userCloudRepository,
            local = localUserDataSource,
            appRepository = appRemoteDataSource,
            userPrefs = userPrefs

        )
    }



//viewModels
    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel(
            authRepository = authRepository,
            userRepository = userCloudRepository,
            sessionManager = sessionManager,
        )
    }

    fun provideAppSelectionViewModel(): AppSelectionViewModel {
        return AppSelectionViewModel(
            userGlobalRepository, appRepository,sessionManager
        )
    }

    fun provideWizardViewModel(): AppWizardViewModel {
        return AppWizardViewModel(appRepository, cloudFilesRepository, sessionManager)
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

    val appRemoteDataSource : AppRemoteDataSource by lazy {
        provideAppRemoteDataSource(httpClient)
    }

    val appRepository: AppRepository by lazy {
        provideAppRepository()
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


    val authViewModel: AuthViewModel by lazy {
        provideAuthViewModel()
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
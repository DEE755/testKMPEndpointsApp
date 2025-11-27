// commonMain/kotlin/di/ServiceLocator.kt
package di

import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.AuthTokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


// 1) Fonctions "ports" à spécialiser par plateforme
expect fun provideEngine(): HttpClientEngineFactory<*>
expect fun provideAuthRepository(client: HttpClient): AuthRepository

expect fun provideUserRepository(client: HttpClient): UserRepository


expect fun provideCloudFilesRepository(client : HttpClient): CloudFilesRepository

expect fun provideLogger(): Logger

expect fun provideAppContextInstance(): Any

expect fun provideTokenProvider(): AuthTokenProvider


// 2) ServiceLocator commun (pas expect) qui compose tout
object ServiceLocator {

//viewModels
    fun provideLogInOutViewModel(): LogInOutViewModel {
        return LogInOutViewModel(
            authRepository = authRepository,
            userRepository = userRepository,
            tokenProvider = tokenProvider
        )
    }



    val publicClient: HttpClient by lazy {
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

    /*fun createHttpClient(tokenProvider: AuthTokenProvider): HttpClient {
        return HttpClient {
            install(DefaultRequest) {
                headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(HttpSend) {
                intercept { request ->
                    val token = tokenProvider.getAccessToken()
                    if (!token.isNullOrBlank()) {
                        request.headers.append(HttpHeaders.Authorization, "Bearer $token")
                    }
                    execute(request)
                }
            }
        }
    }*/

    // Repo fourni par la plateforme, mais construit ici avec le client commun
    val authRepository: AuthRepository by lazy {
        provideAuthRepository(publicClient)
    }

    val userRepository: UserRepository by lazy {
        provideUserRepository(publicClient)
    }

    val cloudFilesRepository by lazy {
        provideCloudFilesRepository(publicClient)
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

}
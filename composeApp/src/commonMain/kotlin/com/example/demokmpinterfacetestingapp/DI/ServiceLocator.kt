// commonMain/kotlin/di/ServiceLocator.kt
package di

import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.Json


// 1) Fonctions "ports" à spécialiser par plateforme
expect fun provideEngine(): HttpClientEngineFactory<*>
expect fun provideAuthRepository(client: HttpClient): AuthRepository

expect fun provideUserRepository(client: HttpClient): UserRepository

expect fun provideLogger(): Logger

// 2) ServiceLocator commun (pas expect) qui compose tout
object ServiceLocator {


    val client: HttpClient by lazy {
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
        provideAuthRepository(client)
    }

    val userRepository: UserRepository by lazy {
        provideUserRepository(client)
    }
}
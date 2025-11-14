package di

import android.util.Log
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.AndroidAuthRepositoryAndroid
import com.example.demokmpinterfacetestingapp.Repository.AndroidUserRepository
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logger


actual fun provideEngine(): HttpClientEngineFactory<*> = OkHttp
actual fun provideAuthRepository(client: HttpClient): AuthRepository =
    AndroidAuthRepositoryAndroid(client)


actual fun provideUserRepository(client: HttpClient): UserRepository =
    AndroidUserRepository(client)

actual fun provideLogger(): Logger = object : Logger {
    override fun log(message: String) {
        Log.d("Ktor-HTTP", message)
    }
}
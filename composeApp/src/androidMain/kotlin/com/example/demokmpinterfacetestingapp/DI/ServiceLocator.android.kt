package com.example.demokmpinterfacetestingapp.DI

import android.content.Context
import android.util.Log

import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.AndroidAuthRepositoryAndroid
import com.example.demokmpinterfacetestingapp.Repository.AndroidUserRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepositoryAndroid
import com.example.demokmpinterfacetestingapp.Repository.UserRepository
import com.example.demokmpinterfacetestingapp.AuthTokenProvider
import com.example.demokmpinterfacetestingapp.util.AuthTokenProviderAndroid
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logger





actual fun provideEngine(): HttpClientEngineFactory<*> = OkHttp
actual fun provideAuthRepository(client: HttpClient): AuthRepository =
    AndroidAuthRepositoryAndroid(client)


actual fun provideUserRepository(client: HttpClient): UserRepository =
    AndroidUserRepository(client)


actual fun provideCloudFilesRepository(client : HttpClient): CloudFilesRepository =
    CloudFilesRepositoryAndroid(client)

actual fun provideLogger(): Logger = object : Logger {
    override fun log(message: String) {
        Log.d("Ktor-HTTP", message)
    }
}

private lateinit var appContext: Context

fun initAppContext(context: Context) {
    appContext = context.applicationContext
}


actual fun provideAppContextInstance(): Any {
    check(::appContext.isInitialized) { "Toast context not initialized" }
    return appContext
}

actual fun provideTokenProvider(): AuthTokenProvider {

    return AuthTokenProviderAndroid(appContext)
}
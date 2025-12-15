package com.example.demokmpinterfacetestingapp.DI

import android.content.Context
import android.util.Log
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.userPrefs

import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Repository.AndroidAuthRepositoryAndroid
import com.example.demokmpinterfacetestingapp.Repository.AndroidUserCloudDataSource
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepositoryAndroid
import com.example.demokmpinterfacetestingapp.Repository.UserCloudDataSource
import com.example.demokmpinterfacetestingapp.Repository.AppRemoteDataSource
import com.example.demokmpinterfacetestingapp.Repository.AndroidAppRemoteDataSource
import com.example.demokmpinterfacetestingapp.Repository.AndroidLocalUserDataSource
import com.example.demokmpinterfacetestingapp.Repository.UserLocalDataSource
//import com.example.demokmpinterfacetestingapp.Repository.UserLocalDataSource
import com.example.demokmpinterfacetestingapp.util.AuthTokenProviderAndroid
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logger




actual fun provideEngine(): HttpClientEngineFactory<*> = OkHttp
actual fun provideAuthRepository(client: HttpClient): AuthRepository =
    AndroidAuthRepositoryAndroid(client, userPrefs = userPrefs)


actual fun provideUserRepository(client: HttpClient, userPrefs1: UserPrefsDataSource): UserCloudDataSource =
    AndroidUserCloudDataSource(client)

actual fun provideAppRemoteDataSource(client: HttpClient): AppRemoteDataSource =
    AndroidAppRemoteDataSource(client)



actual fun provideCloudFilesRepository(clientWithBearer : HttpClient, cleanClient : HttpClient): CloudFilesRepository =
    CloudFilesRepositoryAndroid(clientWithBearer, cleanClient)

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

actual fun provideUserPrefsDataSource(): UserPrefsDataSource {

    return AndroidUserPrefsDataSource(appContext)
}

actual fun provideUserLocalDataSource(): UserLocalDataSource {
    return AndroidLocalUserDataSource(
        userPrefs = userPrefs
    )
}
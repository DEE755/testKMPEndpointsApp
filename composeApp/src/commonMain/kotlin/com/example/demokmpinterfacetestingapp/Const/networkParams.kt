package com.example.demokmpinterfacetestingapp.Const


expect class CustomApiParams {

     companion object{
         fun getBaseUrl() : String}

}



class GoogleSignInParams {
    companion object {
        const val serverClientId = "781042180538-ua2j6sktaok93chedst9tib6j9teg4ok.apps.googleusercontent.com"
        val backendUrl = CustomApiParams.getBaseUrl() + "/google/verify"
    }
}
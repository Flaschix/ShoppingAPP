package com.example.shoppingapp.presentation.sign_in

import com.google.firebase.auth.FirebaseUser

sealed class SignInState {

    object Success: SignInState()

    data class Error(val error: String?): SignInState()

    object Loading: SignInState()

    object Initial: SignInState()
}
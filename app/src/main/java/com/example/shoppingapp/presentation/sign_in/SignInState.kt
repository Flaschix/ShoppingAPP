package com.example.shoppingapp.presentation.sign_in

import com.google.firebase.auth.FirebaseUser

sealed class SignInState {

    data class Success(val user: FirebaseUser): SignInState()

    data class Error(val error: String?): SignInState()

    object Loading: SignInState()

    object Initial: SignInState()
}
package com.example.shoppingapp.presentation.sign_up

import com.example.shoppingapp.domain.entity.User
import com.google.firebase.auth.FirebaseUser

sealed class SignUpState{

    object Success: SignUpState()

    class Error(val error: String?) : SignUpState()

    object Loading: SignUpState()

    object Initial: SignUpState()
}
package com.example.shoppingapp.presentation.sign_in

sealed class SignInValidateState {

    object Success: SignInValidateState()

    class Error(val msg: String): SignInValidateState()
}

data class SignInFieldsState(
    val email: SignInValidateState,
    val password: SignInValidateState
)
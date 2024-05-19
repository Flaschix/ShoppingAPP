package com.example.shoppingapp.presentation.sign_up

sealed class SignUpValidateState {
    object Success: SignUpValidateState()

    class Error(val msg: String): SignUpValidateState()
}

data class SignUpFieldsState(
    val name: SignUpValidateState,
    val surname: SignUpValidateState,
    val email: SignUpValidateState,
    val password: SignUpValidateState
)
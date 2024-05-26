package com.example.shoppingapp.presentation.sign_in.dialog

import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.presentation.sign_up.SignUpState

sealed class ResetPasswordState {

    data class Success(val email: String): ResetPasswordState()

    class Error(val error: String?) : ResetPasswordState()

    object Loading: ResetPasswordState()

    object Initial: ResetPasswordState()
}
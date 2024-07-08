package com.example.shoppingapp.presentation.profile

import com.example.shoppingapp.domain.entity.User

sealed class UserState {

    object Initial: UserState()

    object Loading: UserState()

    class Success(val data: User): UserState()

    class Error(val msg: String): UserState()
}
package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.entity.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(email: String, password: String): StateFlow<ResultNet> {
        return repository.signIn(email, password)
    }
}
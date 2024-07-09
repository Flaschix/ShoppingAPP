package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.entity.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(user: User): StateFlow<ResultNet> {
        return repository.signUp(user)
    }
}
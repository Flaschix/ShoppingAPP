package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): StateFlow<User> {
        return repository.getUser()
    }
}
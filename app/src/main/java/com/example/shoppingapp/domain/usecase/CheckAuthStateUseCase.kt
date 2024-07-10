package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.ResultNet
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(): StateFlow<Boolean> {
        return repository.checkAuthState()
    }
}
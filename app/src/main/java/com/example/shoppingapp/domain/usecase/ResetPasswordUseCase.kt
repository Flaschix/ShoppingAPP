package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.ResultNet
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(email: String): StateFlow<ResultNet> {
        return repository.resetPassword(email)
    }
}
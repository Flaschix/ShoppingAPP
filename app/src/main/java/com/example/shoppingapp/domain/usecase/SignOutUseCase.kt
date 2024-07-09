package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(){
        repository.signOut()
    }
}
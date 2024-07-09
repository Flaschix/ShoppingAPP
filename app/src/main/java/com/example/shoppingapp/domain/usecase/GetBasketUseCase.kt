package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.Product.Product
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetBasketUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(): StateFlow<List<Product>> {
        return repository.getBasket()
    }
}
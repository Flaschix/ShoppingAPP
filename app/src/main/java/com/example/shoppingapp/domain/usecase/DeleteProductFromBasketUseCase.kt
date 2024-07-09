package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.ResultNet
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DeleteProductFromBasketUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(product: Product): StateFlow<ResultNet> {
        return repository.deleteProductFromBasket(product)
    }
}
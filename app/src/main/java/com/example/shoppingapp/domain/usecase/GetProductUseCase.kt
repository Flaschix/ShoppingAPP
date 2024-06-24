package com.example.onlinemarket.domain.usecase

import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.domain.entity.Product.Product
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(id: String): StateFlow<Product?> {
        return repository.getProduct(id)
    }
}
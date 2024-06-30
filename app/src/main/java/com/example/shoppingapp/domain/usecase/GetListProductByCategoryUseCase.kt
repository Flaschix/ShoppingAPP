package com.example.shoppingapp.domain.usecase

import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.domain.entity.Product.Product
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetListProductByCategoryUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(category: String): StateFlow<List<Product>> {
        return repository.getListProductByCategory(category)
    }
}
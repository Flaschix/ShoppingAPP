package com.example.onlinemarket.domain.repository

import com.example.shoppingapp.domain.entity.Product.Product
import kotlinx.coroutines.flow.StateFlow

interface ProductRepository {

    fun getProduct(id: String): StateFlow<Product?>

    fun getListProduct(): StateFlow<List<Product>>

    fun getListProductByCategory(category: String): StateFlow<List<Product>>
}
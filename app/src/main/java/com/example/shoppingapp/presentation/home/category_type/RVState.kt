package com.example.shoppingapp.presentation.home.category_type

import com.example.shoppingapp.domain.entity.Product.Product

sealed class RVState {

    class Success(val data: List<Product>): RVState()

    object Loading: RVState()
    object Initial: RVState()

    class Error(val msg: String): RVState()
}
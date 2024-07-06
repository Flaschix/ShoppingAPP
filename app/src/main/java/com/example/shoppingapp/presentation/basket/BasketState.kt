package com.example.shoppingapp.presentation.basket

import com.example.shoppingapp.domain.entity.Product.Product

sealed class BasketState {

    object Initial: BasketState()

    object Loading: BasketState()

    class Success(val data: List<Product>): BasketState()

    class Error(val msg: String): BasketState()
}
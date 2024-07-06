package com.example.shoppingapp.presentation.shop_item

import com.example.shoppingapp.domain.entity.Product.Product

sealed class AddProductState {

    object Success: AddProductState()

    object Loading: AddProductState()
    object Initial: AddProductState()

    class Error(val msg: String): AddProductState()
}
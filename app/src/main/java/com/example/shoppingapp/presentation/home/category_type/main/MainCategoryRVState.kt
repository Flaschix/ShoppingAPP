package com.example.shoppingapp.presentation.home.category_type.main

import com.example.shoppingapp.domain.entity.Product.Product

sealed class MainCategoryRVState {

    class Success(val data: List<Product>): MainCategoryRVState()

    object Loading: MainCategoryRVState()
    object Initial: MainCategoryRVState()

    class Error(val msg: String): MainCategoryRVState()
}
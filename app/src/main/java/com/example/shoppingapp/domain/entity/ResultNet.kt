package com.example.shoppingapp.domain.entity

sealed class ResultNet {
    data class Success<T>(val data: T) : ResultNet()
    data class Error(val message: String) : ResultNet()
    object Loading : ResultNet()

    object Initial : ResultNet()
}

package com.example.onlinemarket.domain.repository

import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.entity.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    fun signUp(user: User): StateFlow<ResultNet>

    fun getUser(): StateFlow<User>

    fun signIn(email: String, password: String): StateFlow<ResultNet>

    fun signOut()

    fun resetPassword(email: String): StateFlow<ResultNet>

    fun getFavouriteList(phone: String): StateFlow<List<String>>

    fun getBasket(): StateFlow<List<Product>>

    fun addProductToBasket(product: Product): StateFlow<ResultNet>

    fun deleteProductFromBasket(product: Product): StateFlow<ResultNet>

    fun checkAuthState(): StateFlow<Boolean>
}
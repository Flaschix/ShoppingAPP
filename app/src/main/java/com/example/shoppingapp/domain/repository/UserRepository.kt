package com.example.onlinemarket.domain.repository

import com.example.shoppingapp.domain.entity.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    suspend fun addUser(user: User)

    fun getUser(phone: String): StateFlow<User>

    fun editUser(user: User)

    fun getFavouriteList(phone: String): StateFlow<List<String>>
}
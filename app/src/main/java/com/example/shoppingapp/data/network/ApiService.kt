package com.example.shoppingapp.data.network

import com.example.onlinemarket.data.model.dto.ListProductDto
import retrofit2.http.GET

interface ApiService {

    @GET("v3/570f57eb-2e1c-4f87-8a0d-a081f6cb09e0")
    suspend fun loadListProduct(): ListProductDto
}
package com.example.shoppingapp.data.network

import com.example.onlinemarket.data.model.dto.ListProductDto
import retrofit2.http.GET

interface ApiService {

    @GET("v3/0d2883f3-4db6-4374-bdd2-aa365c7cf466")
    suspend fun loadListProduct(): ListProductDto
}
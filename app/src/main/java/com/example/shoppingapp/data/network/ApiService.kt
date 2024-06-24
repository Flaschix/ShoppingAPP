package com.example.shoppingapp.data.network

import com.example.onlinemarket.data.model.dto.ListProductDto
import retrofit2.http.GET

interface ApiService {

    @GET("v3/7fc345e1-d2c1-44de-941f-1e12050219be")
    suspend fun loadListProduct(): ListProductDto
}
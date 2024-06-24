package com.example.onlinemarket.data.model.dto

import com.google.gson.annotations.SerializedName

data class ListProductDto(
    @SerializedName("items") val items: List<ProductDto>
)

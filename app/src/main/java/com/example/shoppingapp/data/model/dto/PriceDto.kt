package com.example.onlinemarket.data.model.dto

import com.google.gson.annotations.SerializedName

data class PriceDto(
    @SerializedName("price") val price: String,
    @SerializedName("discount") val discount: Int,
    @SerializedName("priceWithDiscount") val priceWithDiscount: String,
    @SerializedName("unit") val unit: String
)

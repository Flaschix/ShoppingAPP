package com.example.onlinemarket.data.model.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("images") val images: List<String>, ///добавил
    @SerializedName("price") val price: PriceDto,
    @SerializedName("feedback") val feedback: FeedbackDto,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("available") val available: Int,
    @SerializedName("description") val description: String,
    @SerializedName("info") val info: List<InfoDto>,
    @SerializedName("ingredients") val ingredients: String
)

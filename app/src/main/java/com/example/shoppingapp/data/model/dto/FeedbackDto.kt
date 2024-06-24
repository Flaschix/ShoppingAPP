package com.example.onlinemarket.data.model.dto

import com.google.gson.annotations.SerializedName

data class FeedbackDto(
    @SerializedName("count") val count: Int,
    @SerializedName("rating") val rating: Float
)

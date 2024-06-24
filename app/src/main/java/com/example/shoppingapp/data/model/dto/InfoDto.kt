package com.example.onlinemarket.data.model.dto

import com.google.gson.annotations.SerializedName

data class InfoDto(
    @SerializedName("title") val title: String,
    @SerializedName("value") val value: String
)

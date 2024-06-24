package com.example.shoppingapp.domain.entity.Product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInfo(
    val title: String,
    val value: String
) : Parcelable
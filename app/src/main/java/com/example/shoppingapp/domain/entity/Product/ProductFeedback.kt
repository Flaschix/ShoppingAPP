package com.example.shoppingapp.domain.entity.Product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductFeedback(
    val count: Int,
    val rating: Float
) : Parcelable

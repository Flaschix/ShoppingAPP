package com.example.shoppingapp.presentation.home.category_type

sealed class Category(val category: String) {

    object Mask: Category("mask")
    object Face: Category("face")
    object Suntan: Category("suntan")
    object Body: Category("body")
    object InRoad: Category("inroad")
}
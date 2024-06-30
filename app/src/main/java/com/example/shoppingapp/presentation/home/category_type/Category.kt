package com.example.shoppingapp.presentation.home.category_type

sealed class Category(val category: String) {

    object Chair: Category("chair")
    object Cupboard: Category("Cupboard")
    object Table: Category("table")
    object Bed: Category("Accessory")
    object Illumination: Category("Furniture")
}
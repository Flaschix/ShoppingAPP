package com.example.shoppingapp.domain.entity

data class User(
    val name: String,
    val surname: String,
    val mail: String,
    val password: String
){
    constructor(): this("","","","")
}
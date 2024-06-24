package com.example.shoppingapp.presentation.home.special_product

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppingapp.domain.entity.Product.Product

class ProductDiffCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
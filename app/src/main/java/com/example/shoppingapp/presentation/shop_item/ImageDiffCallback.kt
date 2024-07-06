package com.example.shoppingapp.presentation.shop_item

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppingapp.domain.entity.Product.Product

class ImageDiffCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
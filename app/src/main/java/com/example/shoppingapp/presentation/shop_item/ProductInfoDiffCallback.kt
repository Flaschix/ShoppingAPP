package com.example.shoppingapp.presentation.shop_item

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppingapp.domain.entity.Product.ProductInfo

class ProductInfoDiffCallback: DiffUtil.ItemCallback<ProductInfo>() {
    override fun areItemsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
        return newItem.title == oldItem.title
    }

    override fun areContentsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
        return oldItem == newItem
    }
}
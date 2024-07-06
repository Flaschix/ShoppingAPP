package com.example.shoppingapp.presentation.shop_item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.databinding.RecycleViewInfoItemBinding
import com.example.shoppingapp.domain.entity.Product.ProductInfo

class ShopItemInfoAdapter: ListAdapter<ProductInfo, ShopItemInfoAdapter.ProductInfoViewHolder>(
    ProductInfoDiffCallback()
) {

    inner class ProductInfoViewHolder(val binding: RecycleViewInfoItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(info: ProductInfo){
            binding.apply {
                tvInfoTitle.text = info.title
                tvInfoValue.text = info.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInfoViewHolder {
        return ProductInfoViewHolder(
            RecycleViewInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductInfoViewHolder, position: Int) {
        val info = getItem(position)
        holder.bind(info)
    }
}
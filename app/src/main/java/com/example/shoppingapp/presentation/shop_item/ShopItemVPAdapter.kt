package com.example.shoppingapp.presentation.shop_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.ViewpageShopItemBinding

class ShopItemVPAdapter: ListAdapter<String, ShopItemVPAdapter.ShopItemViewHolder>(
    ImageDiffCallback()
) {

    inner class ShopItemViewHolder(val binding: ViewpageShopItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(imgPath: String){
            Glide.with(itemView).load(imgPath).into(binding.imageProductDetails)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        return ShopItemViewHolder(
            ViewpageShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val imgPath = getItem(position)
        holder.bind(imgPath)
    }


}
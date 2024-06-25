package com.example.shoppingapp.presentation.home.category_type.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.RecycleViewSpecialBinding
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.presentation.home.ProductDiffCallback

class SpecialProductAdapter: ListAdapter<Product, SpecialProductAdapter.SpecialProductViewHolder>(
    ProductDiffCallback()
) {

    var onProductClickListener: ((Product) -> Unit)? = null

    inner class SpecialProductViewHolder(private val binding: RecycleViewSpecialBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageSpecialRvItem)
                tvSpecialProductName.text = product.title
                tvSpecialPrdouctPrice.text = "$ ${product.price.price}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        return SpecialProductViewHolder(
            RecycleViewSpecialBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(product)
        }

    }
}
package com.example.shoppingapp.presentation.home.category_type.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.RecycleViewBestCaseBinding
import com.example.shoppingapp.databinding.RecycleViewBestProductBinding
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.presentation.home.ProductDiffCallback

class BestProductAdapter: ListAdapter<Product, BestProductAdapter.BestProductViewHolder>(
    ProductDiffCallback()
) {

    var onProductClickListener: ((Product) -> Unit)? = null

    inner class BestProductViewHolder(private val binding: RecycleViewBestProductBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                tvNewPrice.text = "$ ${product.price.priceWithDiscount}"
                tvPrice.text = "$ ${product.price.price}"
                tvName.text = product.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            RecycleViewBestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(product)
        }

    }
}
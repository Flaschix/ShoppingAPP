package com.example.shoppingapp.presentation.home.category_type.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.RecycleViewBestCaseBinding
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.presentation.home.ProductDiffCallback

class BestCaseAdapter: ListAdapter<Product, BestCaseAdapter.BestCaseViewHolder>(
ProductDiffCallback()
) {

    var onProductClickListener: ((Product) -> Unit)? = null

    inner class BestCaseViewHolder(private val binding: RecycleViewBestCaseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)
                tvNewPrice.text = "$ ${product.price.priceWithDiscount}"
                tvOldPrice.text = "$ ${product.price.price}"
                tvDealProductName.text = product.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestCaseViewHolder {
        return BestCaseViewHolder(
            RecycleViewBestCaseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: BestCaseViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(product)
        }

    }
}
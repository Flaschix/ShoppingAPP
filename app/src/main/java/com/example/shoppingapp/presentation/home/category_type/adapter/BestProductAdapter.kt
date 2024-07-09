package com.example.shoppingapp.presentation.home.category_type.adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                Log.d("RESPONSE", "$product: \n")
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                tvDiscount.text = "-${product.price.discount}%"
                tvPrice.text = "${product.price.priceWithDiscount} ₽"
                tvOldPrice.text = "${product.price.price} ₽"
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvName.text = product.title
                tvDescription.text = product.description
                tvRating.text = product.feedback.rating.toString()
                tvFeedback.text = "(${product.feedback.count})"
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
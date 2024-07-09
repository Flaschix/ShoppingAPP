package com.example.shoppingapp.presentation.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shoppingapp.databinding.BasketProductItemBinding
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.presentation.home.ProductDiffCallback

class BasketAdapter: ListAdapter<Product, BasketAdapter.BasketViewHolder>(
    ProductDiffCallback()
){
    var deleteClickListener: ((Product) -> Unit)? = null

    var onProductClickListener: ((Product) -> Unit)? = null

    inner class BasketViewHolder(val binding: BasketProductItemBinding): ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageCartProduct)
                tvProductCartName.text = product.title
                tvProductCartPrice.text = "${product.price.priceWithDiscount} ₽"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(
            BasketProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val product = getItem(position)

        holder.bind(product)

        holder.binding.imageMinus.setOnClickListener{
            deleteClickListener?.invoke(product)
        }

        holder.itemView.setOnClickListener {
            onProductClickListener?.invoke(product)
        }
    }
}
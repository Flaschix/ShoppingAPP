package com.example.shoppingapp.data.mapper

import com.example.onlinemarket.data.model.dto.ListProductDto
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.Product.ProductFeedback
import com.example.shoppingapp.domain.entity.Product.ProductInfo
import com.example.shoppingapp.domain.entity.Product.ProductPrice
import javax.inject.Inject

class ProductMapper @Inject constructor() {
    fun mapResponseToProduct(response: ListProductDto): List<Product>{
        val result = mutableListOf<Product>()

        for (item in response.items){
            result.add(
                Product(
                    id = item.id,
                    title = item.title,
                    subtitle = item.subtitle,
                    category = item.category,
                    images = item.images,
                    price = ProductPrice(
                        price = item.price.price,
                        discount = item.price.discount,
                        priceWithDiscount = item.price.priceWithDiscount,
                        unit = item.price.unit
                    ),
                    feedback = ProductFeedback(
                        count = item.feedback.count,
                        rating = item.feedback.rating
                    ),
                    tags = item.tags,
                    available = item.available,
                    description = item.description,
                    info = item.info.map {
                        ProductInfo(
                            title = it.title,
                            value = it.value
                        )
                    },
                    ingredients = item.ingredients
                )
            )
        }

        return result
    }

    fun mapResponseToProductByCategory(response: ListProductDto, category: String): List<Product>{
        val result = mutableListOf<Product>()

        for (item in response.items){
            if (item.tags.contains(category))
            result.add(
                Product(
                    id = item.id,
                    title = item.title,
                    subtitle = item.subtitle,
                    category = item.category,
                    images = item.images,
                    price = ProductPrice(
                        price = item.price.price,
                        discount = item.price.discount,
                        priceWithDiscount = item.price.priceWithDiscount,
                        unit = item.price.unit
                    ),
                    feedback = ProductFeedback(
                        count = item.feedback.count,
                        rating = item.feedback.rating
                    ),
                    tags = item.tags,
                    available = item.available,
                    description = item.description,
                    info = item.info.map {
                        ProductInfo(
                            title = it.title,
                            value = it.value
                        )
                    },
                    ingredients = item.ingredients
                )
            )
        }

        return result
    }
}
package com.example.shoppingapp.data.repository

import android.util.Log
import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.data.mapper.ProductMapper
import com.example.shoppingapp.data.network.ApiService
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val mapper: ProductMapper,
    private val apiService: ApiService
): ProductRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val recommendations: StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()

        val products = mapper.mapResponseToProduct(response)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override fun getProduct(id: String): StateFlow<Product?> {
        TODO("Not yet implemented")
    }

    override fun getListProduct(): StateFlow<List<Product>> = recommendations

    override fun getListProductByCategory(category: String): StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()
        val products = mapper.mapResponseToProductByCategory(response, category)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

}
package com.example.shoppingapp.di

import android.content.Context
import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.data.network.ApiFactory
import com.example.shoppingapp.data.network.ApiService
import com.example.shoppingapp.data.repository.ProductRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiFactory.apiService
    }
}
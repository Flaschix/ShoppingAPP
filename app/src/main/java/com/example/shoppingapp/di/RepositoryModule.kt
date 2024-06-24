package com.example.shoppingapp.di

import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideMainRepositoryImpl(repository: ProductRepositoryImpl): ProductRepository
}
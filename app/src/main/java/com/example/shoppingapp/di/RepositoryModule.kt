package com.example.shoppingapp.di

import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.data.repository.ProductRepositoryImpl
import com.example.shoppingapp.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideMainRepositoryImpl(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    fun provideUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository
}
package com.example.payments.di

import com.example.local.room.UserDao
import com.example.payments.data.PaymentsRepository
import com.example.payments.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PaymentsModule {

    @Provides
    @Singleton
    internal fun providePaymentsRepository(
        userDao: UserDao
    ): PaymentRepository = PaymentsRepository(userDao)
}
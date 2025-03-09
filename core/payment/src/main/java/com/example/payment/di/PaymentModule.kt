package com.example.payment.di

import android.content.Context
import com.example.payment.data.IPaymentService
import com.example.payment.data.PaymentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Provides
    @Singleton
    fun providePaymentService(
        @ApplicationContext context: Context
    ): IPaymentService = PaymentService(context)
}
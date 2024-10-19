package com.example.payments.domain.repository

import com.example.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun updateWalletAmount(userId: String, amount: Double)
    suspend fun getUserWalletBalance(userId: String): Flow<Double>
    suspend fun getUser(userId: String): UserEntity?
}
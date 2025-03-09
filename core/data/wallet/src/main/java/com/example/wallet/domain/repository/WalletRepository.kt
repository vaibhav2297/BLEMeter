package com.example.wallet.domain.repository

import com.example.local.model.UserEntity
import com.example.wallet.domain.model.TransactionType
import com.example.wallet.domain.model.WalletResponse
import com.example.wallet.domain.model.WalletTransactionResponse
import com.example.wallet.domain.model.request.WalletRequest
import com.example.wallet.domain.model.request.WalletTransactionRequest
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun updateWalletAmount(userId: String, amount: Double, transactionType: TransactionType)
    suspend fun getUserWalletBalance(userId: String): Flow<Double>
    suspend fun getUser(userId: String): UserEntity?
    suspend fun getWalletTransactions(): Result<WalletTransactionResponse>
    suspend fun insertWalletTransaction(walletTransactionRequest: WalletTransactionRequest): Result<WalletTransactionResponse>
    suspend fun getWallet(): Result<WalletResponse>
}
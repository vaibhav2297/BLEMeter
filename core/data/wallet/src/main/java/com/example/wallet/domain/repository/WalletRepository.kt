package com.example.wallet.domain.repository

import com.example.local.model.UserEntity
import com.example.wallet.domain.model.TransactionType
import com.example.wallet.domain.model.Wallet
import com.example.wallet.domain.model.WalletTransaction
import com.example.wallet.domain.model.request.WalletTransactionRequest
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun updateWalletAmount(userId: String, amount: Double, transactionType: TransactionType)
    suspend fun getUserWalletBalance(userId: String): Flow<Double>
    suspend fun getUser(userId: String): UserEntity?
    suspend fun getWalletTransactions(): Result<List<WalletTransaction>>
    suspend fun insertWalletTransaction(walletTransactionRequest: WalletTransactionRequest): Result<Unit>
    suspend fun getWallet(): Result<List<Wallet>>
    suspend fun getWalletId(): String
}
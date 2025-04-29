package com.example.wallet.data

import com.example.local.model.UserEntity
import com.example.wallet.domain.model.TransactionType
import com.example.wallet.domain.model.request.WalletTransactionRequest
import com.example.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class WalletRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : WalletRepository {

    override suspend fun updateWalletAmount(
        userId: String,
        amount: Double,
        transactionType: TransactionType
    ) =
        localDataSource.updateWalletAmount(
            userId = userId,
            amount = amount
        )

    override suspend fun getUserWalletBalance(userId: String): Flow<Double> =
        localDataSource.getUserWalletBalance(userId)

    override suspend fun getUser(userId: String): UserEntity? =
        localDataSource.getUser(userId)

    override suspend fun getWalletTransactions() = remoteDataSource.getWalletTransactions()

    override suspend fun insertWalletTransaction(walletTransactionRequest: WalletTransactionRequest) =
        remoteDataSource.insertWalletTransactions(walletTransactionRequest).onSuccess {
            getWallet(walletTransactionRequest.userId).getOrNull()?.let { wallet ->
                localDataSource.updateWalletAmount(
                    userId = walletTransactionRequest.userId,
                    amount = wallet.balance
                )
            }
        }

    override suspend fun getWallet(userId: String) =
        remoteDataSource.getWallet()
            .mapCatching { wallets ->
                wallets.firstOrNull()
                    ?: throw NoSuchElementException("No wallet found for user $userId")
            }
            .onSuccess { wallet ->
                localDataSource.updateWalletAmount(
                    userId = userId,
                    amount = wallet.balance
                )
            }

    override suspend fun getWalletId(userId: String): String {
        return localDataSource.getUserWallet().first().let { walletId ->
            walletId.ifEmpty {
                getWallet(userId).getOrNull()?.id ?: ""
            }
        }
    }
}
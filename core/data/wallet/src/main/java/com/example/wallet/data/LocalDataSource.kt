package com.example.wallet.data

import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.local.model.UserEntity
import com.example.local.room.UserDao
import com.example.wallet.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalDataSource @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: IAppDataStore
) {

    suspend fun updateWalletAmount(
        userId: String,
        amount: Double,
        transactionType: TransactionType
    ) {
        userDao.getUser(userId)?.let { user ->
            val updatedAmount = transactionType.updateBalance(user.walletAmount, amount)
            userDao.updateUser(user.copy(walletAmount = updatedAmount))
        }
    }

    suspend fun getUserWalletBalance(userId: String): Flow<Double> =
        userDao.getUsers().map { users ->
            users.firstOrNull { user -> user.id == userId }?.walletAmount ?: 0.0
        }

    suspend fun getUser(userId: String): UserEntity? =
        userDao.getUser(userId)

    suspend fun getUserWallet() =
        dataStore.getPreference(DataStoreKeys.USER_WALLET_ID, "")
}
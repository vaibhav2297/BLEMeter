package com.example.payments.data

import com.example.local.model.UserEntity
import com.example.local.room.UserDao
import com.example.payments.domain.model.TransactionType
import com.example.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PaymentsRepository @Inject constructor(
    private val userDao: UserDao
) : PaymentRepository {

    override suspend fun updateWalletAmount(userId: String, amount: Double, transactionType: TransactionType) {
        userDao.getUser(userId)?.let { user ->
            val updatedAmount = transactionType.updateBalance(user.walletAmount, amount)
            userDao.updateUser(user.copy(walletAmount = updatedAmount))
        }
    }

    override suspend fun getUserWalletBalance(userId: String): Flow<Double> =
        userDao.getUsers().map { users ->
            users.firstOrNull { user -> user.id == userId }?.walletAmount ?: 0.0
        }

    override suspend fun getUser(userId: String): UserEntity? =
        userDao.getUser(userId)
}
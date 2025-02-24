package com.example.transactions.data

import com.example.transactions.domain.model.request.MeterTransactionRequest
import com.example.transactions.domain.repository.IMeterTransactionRepository
import javax.inject.Inject

internal class MeterTransactionRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : IMeterTransactionRepository {

    override suspend fun getMeterTransactionsByUser(
        userId: String
    ) = remoteDataSource.getMeterTransactionsByUser(userId)

    override suspend fun insertMeterTransaction(
        meterTransactionRequest: MeterTransactionRequest
    ) = remoteDataSource.insertMeterTransaction(meterTransactionRequest)
}
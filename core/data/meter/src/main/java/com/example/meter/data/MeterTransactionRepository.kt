package com.example.meter.data

import com.example.meter.domain.model.request.MeterLogRequest
import com.example.meter.domain.model.request.MeterTransactionRequest
import com.example.meter.domain.repository.IMeterTransactionRepository
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

    override suspend fun insertMeterLogs(
        meterLogRequest: MeterLogRequest
    ) = remoteDataSource.insertMeterLogs(meterLogRequest)
}
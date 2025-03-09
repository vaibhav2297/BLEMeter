package com.example.meter.domain.repository

import com.example.meter.domain.model.MeterTransaction
import com.example.meter.domain.model.request.MeterLogRequest
import com.example.meter.domain.model.request.MeterTransactionRequest

interface IMeterTransactionRepository {
    suspend fun getMeterTransactionsByUser(userId: String): Result<List<MeterTransaction>>
    suspend fun insertMeterTransaction(meterTransactionRequest: MeterTransactionRequest): Result<Unit>
    suspend fun insertMeterLogs(meterLogRequest: MeterLogRequest): Result<Unit>
}
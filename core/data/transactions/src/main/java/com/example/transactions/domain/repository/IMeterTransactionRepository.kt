package com.example.transactions.domain.repository

import com.example.transactions.domain.model.MeterTransactionResponse
import com.example.transactions.domain.model.request.MeterTransactionRequest

interface IMeterTransactionRepository {
    suspend fun getMeterTransactionsByUser(userId: String): Result<MeterTransactionResponse>
    suspend fun insertMeterTransaction(meterTransactionRequest: MeterTransactionRequest): Result<Unit>
}
package com.example.transactions.data

import com.example.network.config.safeRequest
import com.example.network.ktor.KtorClient
import com.example.network.model.SupabaseApis
import com.example.transactions.domain.model.MeterTransactionResponse
import com.example.transactions.domain.model.request.MeterTransactionRequest
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val ktorClient: KtorClient
) {

    private val sourceUrl = SupabaseApis.METER_TRANSACTION.url

    suspend fun getMeterTransactionsByUser(
        userId: String
    ): Result<MeterTransactionResponse> =
        ktorClient.client.safeRequest<MeterTransactionResponse> {
            url(sourceUrl)
            method = HttpMethod.Get
            parameter("user_id", "eq.${userId}")
            parameter("select", "*")
        }

    suspend fun insertMeterTransaction(
        meterTransactionRequest: MeterTransactionRequest
    ): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(sourceUrl)
            method = HttpMethod.Post
            setBody(meterTransactionRequest)
        }
}
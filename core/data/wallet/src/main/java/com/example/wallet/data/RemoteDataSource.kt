package com.example.wallet.data

import com.example.network.config.safeRequest
import com.example.network.ktor.KtorClient
import com.example.network.model.SupabaseApis
import com.example.wallet.domain.model.WalletResponse
import com.example.wallet.domain.model.WalletTransactionResponse
import com.example.wallet.domain.model.request.WalletRequest
import com.example.wallet.domain.model.request.WalletTransactionRequest
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val ktorClient: KtorClient
) {

    private val walletTransactionUrl = SupabaseApis.WALLET_TRANSACTION.url
    private val walletUrl = SupabaseApis.WALLET.url

    suspend fun getWalletTransactions(): Result<WalletTransactionResponse> =
        ktorClient.client.safeRequest<WalletTransactionResponse> {
            url(walletTransactionUrl)
            method = HttpMethod.Get
        }

    suspend fun insertWalletTransactions(
        walletTransactionRequest: WalletTransactionRequest
    ): Result<Unit> =
        ktorClient.client.safeRequest<Unit> {
            url(walletTransactionUrl)
            method = HttpMethod.Post
            setBody(walletTransactionRequest)
        }

    suspend fun getWallet(): Result<WalletResponse> =
        ktorClient.client.safeRequest<WalletResponse> {
            url(walletUrl)
            method = HttpMethod.Get
        }
}
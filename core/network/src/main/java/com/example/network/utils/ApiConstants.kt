package com.example.network.utils

internal object ApiConstants {

    object Supabase {
        const val BASE_URL = "https://irrhqpjwtgmdpxjcriit.supabase.co"
        const val TRANSACTION_END_POINT = "/rest/v1/transactions"
        const val WALLET_END_POINT = "/rest/v1/wallet"
        const val SIGNUP_END_POINT = "/auth/v1/signup"
        const val LOGIN_END_POINT = "/auth/v1/token"
        const val METER_TRANSACTION_END_POINT = "/rest/v1/meter_transactions"
    }
}

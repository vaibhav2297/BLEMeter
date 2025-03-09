package com.example.network.model

import com.example.network.utils.ApiConstants

enum class SupabaseApis {
    SIGN_UP,
    LOGIN,
    WALLET,
    TRANSACTION,
    METER_LOGS,
    METER,
    METER_TRANSACTION,
    WALLET_TRANSACTION;

    val url: String
        get() {
            val endPoint = when (this) {
                SIGN_UP -> ApiConstants.Supabase.SIGNUP_END_POINT
                LOGIN -> ApiConstants.Supabase.LOGIN_END_POINT
                WALLET -> ApiConstants.Supabase.WALLET_END_POINT
                TRANSACTION -> ApiConstants.Supabase.TRANSACTION_END_POINT
                METER_TRANSACTION -> ApiConstants.Supabase.METER_TRANSACTION_END_POINT
                METER -> ApiConstants.Supabase.METER_END_POINT
                METER_LOGS -> ApiConstants.Supabase.METER_LOGS_END_POINT
                WALLET_TRANSACTION -> ApiConstants.Supabase.WALLET_TRANSACTION_END_POINT
            }

            return ApiConstants.Supabase.BASE_URL + endPoint
        }
}
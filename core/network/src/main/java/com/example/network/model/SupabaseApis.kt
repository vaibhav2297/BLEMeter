package com.example.network.model

import com.example.network.utils.ApiConstants

/**
 * Enum class representing the various API endpoints for interacting with the Supabase backend.
 */
enum class SupabaseApis {
    SIGN_UP,
    LOGIN,
    WALLET,
    TRANSACTION,
    METER_LOGS,
    METER,
    METER_TRANSACTION,
    WALLET_TRANSACTION,
    USER_PROFILE;

    /**
     * Constructs the full URL for the API endpoint.
     *
     * This property dynamically generates the URL by combining the base URL
     * (from [ApiConstants.Supabase.BASE_URL]) with the specific endpoint path
     * corresponding to the enum constant.
     *
     * @return The full URL for the API endpoint as a [String].
     */
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
                USER_PROFILE -> ApiConstants.Supabase.USER_PROFILE_END_POINT
            }

            return ApiConstants.Supabase.BASE_URL + endPoint
        }
}
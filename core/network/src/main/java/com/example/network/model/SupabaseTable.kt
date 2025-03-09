package com.example.network.model

enum class SupabaseTable(
    val tableName: String
) {
    METER("meter"),
    METER_LOGS("meter_logs"),
    WALLET_TRANSACTIONS("wallet_transactions"),
    USER_INFO("user_info"),
    WALLET("wallet"),
}
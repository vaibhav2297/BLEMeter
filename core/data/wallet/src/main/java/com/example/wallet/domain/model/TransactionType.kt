package com.example.wallet.domain.model

enum class TransactionType {
    CREDIT,
    DEBIT;

    fun updateBalance(totalBalance: Double, amount: Double): Double {
        return when (this) {
            CREDIT -> {
                totalBalance + amount
            }

            DEBIT -> {
                (totalBalance - amount).coerceAtLeast(0.0)
            }
        }
    }
}
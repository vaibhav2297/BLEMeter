package com.example.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    val email: String,
    val phone: String,
    val role: String,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean,
    val isPhoneVerified: Boolean,
    val updatedAt: String,
    val createdAt: String,

    //extra info
    val walletAmount: Double = 0.0,
    val meterNumber: String = ""
)

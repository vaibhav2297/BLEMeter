package com.example.user.domain.model

import com.example.local.model.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(

    val id: String,

    val email: String,

    @SerialName("is_admin")
    val isAdmin: Boolean,

    @SerialName("liters_per_rupees")
    val litersPerRupees: Double,

    val aud: String,

    @SerialName("confirmation_sent_at")
    val confirmationSentAt: String = "",

    @SerialName("is_anonymous")
    val isAnonymous: Boolean,

    val phone: String = "",

    val role: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("created_at")
    val createdAt: String,
)

fun toUser(userResponse: UserResponse, userProfile: UserProfile) = User(
    id = userResponse.id,
    email = userResponse.email,
    isAdmin = userProfile.isAdmin,
    litersPerRupees = userProfile.litersPerRupees,
    aud = userResponse.aud,
    isAnonymous = userResponse.isAnonymous,
    createdAt = userResponse.createdAt,
    updatedAt = userResponse.updatedAt,
    role = userResponse.role,
    phone = userResponse.phone,
    confirmationSentAt = userResponse.confirmationSentAt
)

fun User.toUserEntity() = UserEntity(
    id = this.id,
    isAdmin = this.isAdmin,
    email = this.email,
    phone = this.phone,
    role = this.role,
    isAnonymous = this.isAnonymous,
    isEmailVerified = false,
    isPhoneVerified = false,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
    litersPerRupees = this.litersPerRupees ?: 0.0,
    aud = this.aud
)

fun UserEntity.toUser() = User(
    id = this.id,
    isAdmin = this.isAdmin,
    email = this.email,
    phone = this.phone,
    role = this.role,
    isAnonymous = this.isAnonymous,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
    litersPerRupees = this.litersPerRupees,
    aud = this.aud
)
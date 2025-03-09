package com.example.meter.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeterLog(

    @SerialName("accumulated_usage")
    val accumulatedUsage: Int,

    @SerialName("additional_deductions")
    val additionalDeductions: Int,

    @SerialName("alarm_variables")
    val alarmVariables: Int,

    @SerialName("battery_voltage")
    val batteryVoltage: String,

    @SerialName("calibration_identification")
    val calibrationIdentification: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("id")
    val id: String,

    @SerialName("inplace_method")
    val inplaceMethod: String,

    @SerialName("meter_id")
    val meterId: String,

    @SerialName("minimum_usage_variable")
    val minimumUsageVariable: Int,

    @SerialName("overdraft_variables")
    val overdraftVariables: Int,

    @SerialName("payment_method")
    val paymentMethod: String,

    @SerialName("product_version")
    val productVersion: Int,

    @SerialName("program_version")
    val programVersion: Int,

    @SerialName("purchase_frequency")
    val purchaseFrequency: Int,

    @SerialName("surplus_variable")
    val surplusVariable: Int,

    @SerialName("total_purchase")
    val totalPurchase: Int,

    @SerialName("user_id")
    val userId: String,

    @SerialName("valve_status")
    val valveStatus: String
)

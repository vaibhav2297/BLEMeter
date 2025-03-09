package com.example.meter.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeterLogRequest(

    @SerialName("accumulated_usage")
    val accumulatedUsage: Double,

    @SerialName("additional_deductions")
    val additionalDeductions: Int,

    @SerialName("alarm_variables")
    val alarmVariables: Int,

    @SerialName("battery_voltage")
    val batteryVoltage: String,

    @SerialName("calibration_identification")
    val calibrationIdentification: String,

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

    @SerialName("program_version")
    val programVersion: Int,

    @SerialName("purchase_frequency")
    val purchaseFrequency: Int,

    @SerialName("surplus_variable")
    val surplusVariable: Double,

    @SerialName("total_purchase")
    val totalPurchase: Double,

    @SerialName("user_id")
    val userId: String,

    @SerialName("valve_status")
    val valveStatus: String
)

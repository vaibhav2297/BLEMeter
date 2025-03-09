package com.example.payment.data

import android.app.Activity
import android.content.Context
import com.razorpay.Checkout
import com.razorpay.PayloadHelper
import javax.inject.Inject

internal class PaymentService @Inject constructor(
    private val context: Context
) : IPaymentService {

    companion object {
        const val CURRENCY = "INR"
    }

    private lateinit var checkout: Checkout

    override fun initialisePayment() {
        Checkout.preload(context)

        checkout = Checkout()
    }

    override fun setKeyID(key: String) {
        require(::checkout.isInitialized) { "payment service is not initialised" }

        checkout.setKeyID(key)
    }

    override fun openPayment(activity: Activity) {
        require(::checkout.isInitialized) { "payment service is not initialised" }

        try {
            PayloadHelper(
                currency = CURRENCY,
                amount = 100,
                orderId = ""
            ).apply {
                name = "Vaibhav Patel"
            }.run {
                checkout.open(activity, this.getJson())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
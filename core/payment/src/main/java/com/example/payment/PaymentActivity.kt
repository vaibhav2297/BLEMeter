package com.example.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.payment.domain.PaymentOptions
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener


class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener {

    private lateinit var checkout: Checkout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paymentOptions = intent.getParcelableExtra<PaymentOptions>(PAYMENT_OPTION)

        initPayment()

        paymentOptions?.let { openPayment(paymentOptions) }
    }

    private fun initPayment() {
        Checkout.preload(this)
        checkout = Checkout()
        checkout.setKeyID(TEST_KEY)
    }

    private fun openPayment(option: PaymentOptions) {
        if (::checkout.isInitialized) {
            try {
                Log.d(TAG, "openPayment: ${option.toJSON()}")
                checkout.open(this@PaymentActivity, option.toJSON())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onPaymentSuccess(razorPayPaymentId: String?, paymentData: PaymentData?) {
        Log.d(TAG, "onPaymentSuccess: $paymentData")

        val resultIntent = Intent().apply {
            putExtra(IS_PAYMENT_SUCCEED, true)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }

    override fun onPaymentError(errorCode: Int, response: String?, data: PaymentData?) {
        Log.d(TAG, "onPaymentError: $errorCode :: response :: $response")

        val resultIntent = Intent().apply {
            putExtra(IS_PAYMENT_SUCCEED, false)
            putExtra(PAYMENT_ERROR, response)
            putExtra(PAYMENT_ERROR_CODE, errorCode)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val TAG = "PaymentActivity"
        const val CURRENCY = "INR"
        const val TEST_KEY = "rzp_test_NeREBciyHyczNU"

        const val IS_PAYMENT_SUCCEED = "paymentSucceed"
        const val PAYMENT_ERROR = "paymentError"
        const val PAYMENT_ERROR_CODE = "paymentErrorCode"
        const val PAYMENT_OPTION = "paymentOptions"
    }
}
package com.example.payment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.payment.data.IPaymentService
import com.example.payment.data.PaymentService
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import javax.inject.Inject

class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener {

    @Inject
    lateinit var paymentService: IPaymentService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initPayment()
    }

    private fun initPayment() {
        paymentService.apply {
            initialisePayment()
            setKeyID("")
            openPayment(this@PaymentActivity)
        }
    }

    override fun onPaymentSuccess(razorPayPaymentId: String?, paymentData: PaymentData?) {
        Log.d(TAG, "onPaymentSuccess: $paymentData")
    }

    override fun onPaymentError(errorCode: Int, response: String?, data: PaymentData?) {
        Log.d(TAG, "onPaymentError: $errorCode")
    }

    companion object {
        const val TAG = "PaymentActivity"
    }
}
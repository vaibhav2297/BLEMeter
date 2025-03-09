package com.example.payment.data

import android.app.Activity

interface IPaymentService {

    fun initialisePayment()
    fun setKeyID(key: String)
    fun openPayment(activity: Activity)
}
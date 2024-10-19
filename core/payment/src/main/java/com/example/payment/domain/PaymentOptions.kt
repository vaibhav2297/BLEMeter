package com.example.payment.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class PaymentOptions(
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
    val currency: String = "INR",
    val amount: Int,
    val prefill: Prefill? = Prefill(),
    val retry: Retry? = Retry()
) : Parcelable {

    fun toJSON(): JSONObject {
        val jsonObject = JSONObject()

        name?.let { jsonObject.put("name", it) }
        description?.let { jsonObject.put("description", it) }
        image?.let { jsonObject.put("image", it) }
        jsonObject.put("currency", currency)
        jsonObject.put("amount", amount)
        prefill?.let { jsonObject.put("prefill", it.toJSON()) }
        retry?.let { jsonObject.put("retry", it.toJSON()) }

        return jsonObject
    }

    companion object {
        fun fromJSON(jsonObject: JSONObject): PaymentOptions {
            return PaymentOptions(
                name = jsonObject.optString("name", null),
                description = jsonObject.optString("description", null),
                image = jsonObject.optString("image", null),
                currency = jsonObject.optString("currency", null),
                amount = if (jsonObject.has("amount")) jsonObject.getInt("amount") else 0,
                prefill = if (jsonObject.has("prefill")) Prefill.fromJSON(jsonObject.getJSONObject("prefill")) else null
            )
        }
    }
}

@Parcelize
data class Prefill(
    val email: String? = null,
    val contact: String? = null,
    val cardNum: String? = null,
    val cardCVV: String? = null,
    val cardExp: String? = null
) : Parcelable {

    fun toJSON(): JSONObject {
        val jsonObject = JSONObject()

        email?.let { jsonObject.put("email", it) }
        contact?.let { jsonObject.put("contact", it) }
        cardNum?.let { jsonObject.put("card_num", it) }
        cardCVV?.let { jsonObject.put("card_cvv", it) }
        cardExp?.let { jsonObject.put("card_exp", it) }

        return jsonObject
    }

    companion object {
        fun fromJSON(jsonObject: JSONObject): Prefill {
            return Prefill(
                email = jsonObject.optString("email", null),
                contact = jsonObject.optString("contact", null),
                cardNum = jsonObject.optString("card_num", null),
                cardExp = jsonObject.optString("card_exp", null),
                cardCVV = jsonObject.optString("card_cvv", null)
            )
        }
    }
}

@Parcelize
data class Retry(
    val enabled: Boolean? = null,
    val maxCount: Int? = null
) : Parcelable {

    fun toJSON(): JSONObject {
        val jsonObject = JSONObject()

        enabled?.let { jsonObject.put("enable", it) }
        maxCount?.let { jsonObject.put("max_count", it) }

        return jsonObject
    }

    companion object {
        fun fromJSON(jsonObject: JSONObject): Retry {
            return Retry(
                enabled = jsonObject.optBoolean("enable", false),
                maxCount = jsonObject.optInt("max_count", 0)
            )
        }
    }
}



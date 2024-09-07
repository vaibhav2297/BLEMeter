package com.example.blemeter.config.extenstions

import kotlin.jvm.Throws


fun String.chunkAndReverseString(): String {
    check(length % 2 == 0) { "Must have an even length" }

    return this
        .chunked(2)
        .reversed()
        .joinToString("")
}

@Throws
fun String.getMeterAddress(): String {
    check(this.length >= 14 ) { "Not enough size to extract meter address" }

    return this.takeLast(14)
}

@Throws
fun String.getMeterType(): String {
    check(this.length == 14 ) { "Size of meter address is not proper" }

    return this.substring(8..9)
}

fun String.isDecimal() =
    this.matches("\\d+(?:\\.\\d+)?".toRegex())
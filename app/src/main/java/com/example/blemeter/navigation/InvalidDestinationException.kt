package com.example.blemeter.navigation

object InvalidDestinationDefaults {
    const val ERROR_MESSAGE = "Invalid destination"
}

data class InvalidDestinationException(
    override val message: String = InvalidDestinationDefaults.ERROR_MESSAGE
) : Exception()

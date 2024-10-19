package com.example.designsystem.utils

/**
 * Sealed interface representing various states of a screen.
 *
 * @param T The type of data expected when the screen is in a success state.
 *
 * Screen states:
 * - Loading: Represents a loading state where no data is available.
 * - Success: Represents a successful state where data is available.
 * - Error: Represents an error state with an associated error message.
 * - None: Represents an idle or uninitialized state where no action has been taken yet.
 */
sealed interface ScreenState<out T> {

    /**
     * Represents the loading state, where the screen is waiting for data.
     */
    data object Loading : ScreenState<Nothing>

    /**
     * Represents a successful state, containing the requested data.
     *
     * @param data The data received from a successful operation.
     */
    data class Success<T>(
        val data: T
    ) : ScreenState<T>

    /**
     * Represents an error state, containing an error message.
     *
     * @param error A string representing the error message.
     */
    data class Error(val error: String) : ScreenState<Nothing>

    /**
     * Represents an idle state where no action or data is available.
     */
    data object None : ScreenState<Nothing>
}

/**
 * Function to check if the screen state is Success.
 *
 * @return true if the current state is Success, otherwise false.
 */
fun <T> ScreenState<T>.isSuccess() = this is ScreenState.Success

/**
 * Function to check if the screen state is Error.
 *
 * @return true if the current state is Error, otherwise false.
 */
fun <T> ScreenState<T>.isFailure() = this is ScreenState.Error

/**
 * Function to retrieve the data from a Success state.
 *
 * @return The data if the state is Success, otherwise null.
 */
fun <T> ScreenState<T>.getDataOrNull(): T? {
    return when (this) {
        is ScreenState.Success -> data
        else -> null
    }
}

/**
 * Function to retrieve the error message from an Error state.
 *
 * @return The error message if the state is Error, otherwise null.
 */
fun <T> ScreenState<T>.getErrorOrNull(): String? {
    return when (this) {
        is ScreenState.Error -> error
        else -> null
    }
}
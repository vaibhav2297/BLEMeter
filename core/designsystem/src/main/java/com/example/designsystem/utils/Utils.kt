package com.example.designsystem.utils

inline fun <T, R> Result<T>.flatMap(transform: (value: T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { value -> transform(value) },
        onFailure = { error -> Result.failure(error) }
    )
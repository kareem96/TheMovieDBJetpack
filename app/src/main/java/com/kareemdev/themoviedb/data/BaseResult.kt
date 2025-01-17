package com.kareemdev.themoviedb.data

sealed class BaseResult<out T> {
    data class Success<out T>(val data: T) : BaseResult<T>()
    data class Failed(val error: Throwable) : BaseResult<Nothing>()
    object Loading : BaseResult<Nothing>()
    object Idle : BaseResult<Nothing>()
}
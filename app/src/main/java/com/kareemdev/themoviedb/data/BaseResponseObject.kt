package com.kareemdev.themoviedb.data

import com.google.gson.annotations.SerializedName

data class BaseResponseObject<T>(
    @SerializedName("results")
    val results: T? = null,
    @SerializedName("page")
    val page: String? = null,
    @SerializedName("total_pages")
    val totalPages: String? = null,
    @SerializedName("total_results")
    val totalResults: String? = null,
)


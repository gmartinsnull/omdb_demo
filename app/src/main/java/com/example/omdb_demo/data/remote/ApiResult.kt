package com.example.omdb_demo.data.remote

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 *  wrapper class that handles API call responses
 */
data class ApiResult<T>(
    val status: Status,
    val data: T?,
    val error: ApiError?
) {
    companion object {
        fun <T> success(data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, data, null)
        }

        fun <T> error(error: ApiError): ApiResult<T> {
            return ApiResult(Status.ERROR, null, error)
        }
    }
}

data class ApiError(
    val code: Int,
    val message: String
)

enum class Status {
    SUCCESS,
    ERROR
}

/**
 * handles api call response returning a parsed ApiResult type
 */
suspend fun <T> handleApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful)
                ApiResult.success(response.body())
            else
                ApiResult.error(ApiError(response.code(), response.message()))
        } catch (e: Exception) {
            when (e) {
                is JsonEncodingException -> ApiResult.error(
                    ApiError(
                        0,
                        "Something went wrong! Potential malformed API response."
                    )
                )

                is JsonDataException -> ApiResult.error(
                    ApiError(
                        0,
                        "Something went wrong! Potential mislabeled data in API response."
                    )
                )

                else -> ApiResult.error(ApiError(0, e.message ?: "Unknown error"))
            }
        }
    }
}
package jp.ac.jec.cm0138.understandme.Helper

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import retrofit2.Response

object LollipopAPIHelper {

    fun<T> handleAPIResponse(
        response: Response<APIResponse<T>>
    ): APIResponse<T> {
        if (!response.isSuccessful) {
            throw IllegalStateException(
                "Network request failed with code: ${response.code()}. Message: ${response.message()}"
            )
        }

        val body =
            response.body() ?: throw IllegalStateException("Response body is null.")

        if (body.status != "success") {
            throw IllegalStateException(body.message ?: "Unknown error from API.")
        }

        return body

    }
}
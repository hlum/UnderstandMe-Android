package jp.ac.jec.cm0138.understandme.Retrofit

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val newRequest = original.newBuilder()
            .addHeader("Authorization", apiKey)
            .build()

        return chain.proceed(newRequest)
    }
}
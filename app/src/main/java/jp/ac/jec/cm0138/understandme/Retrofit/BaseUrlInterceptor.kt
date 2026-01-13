package jp.ac.jec.cm0138.understandme.Retrofit

import jp.ac.jec.cm0138.understandme.Helper.RemoteConfigManager
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseUrlInterceptor @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newBaseUrl = remoteConfigManager.apiEndpoint.toHttpUrlOrNull()
            ?: return chain.proceed(originalRequest)

        val newUrl = originalUrl.newBuilder()
            .scheme(newBaseUrl.scheme)
            .host(newBaseUrl.host)
            .port(newBaseUrl.port)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}

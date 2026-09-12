package my.vladpustovalov.tdp.data.network

import my.vladpustovalov.tdp.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        
        tokenManager.getToken()?.let { token ->
            val headerValue = if (token.startsWith("Bearer ", ignoreCase = true)) {
                token
            } else {
                token
            }
            requestBuilder.addHeader("Authorization", headerValue)
        }
        
        return chain.proceed(requestBuilder.build())
    }
}

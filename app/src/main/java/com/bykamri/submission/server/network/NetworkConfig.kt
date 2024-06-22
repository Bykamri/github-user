package com.bykamri.submission.server.network

import android.util.Log
import com.bykamri.submission.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {
    private val BASE_URL = BuildConfig.BASE_URL
    private val API_KEY: String = BuildConfig.API_KEY

    class KeyInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (API_KEY.isEmpty()) {
                Log.w("NetworkConfig", "Key is empty")
                return  chain.proceed(chain.request())
            }

            val req: Request = chain.request().newBuilder()
                .header("Authorization", "Bearer $API_KEY")
                .build()

            return chain.proceed(req)
        }
    }

    fun getNetworkService(): NetworkService {
        val logInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

        val keyInterceptor = KeyInterceptor()

        val user = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .addInterceptor(keyInterceptor)
            .build()

        val network = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(user)
            .build()

        return network.create(NetworkService::class.java)
    }

}
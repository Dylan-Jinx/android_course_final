package com.example.final_535_app.utils

import com.example.final_535_app.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object HttpUtils {
    private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

    private val clent = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
//            .baseUrl("http://192.168.123.52:8080")
            .baseUrl("http://192.168.43.175:8080")
//            .baseUrl("http://192.168.2.8:8080")
//            .baseUrl("http://10.22.106.186:8080")
//            .baseUrl("http://10.22.170.161:8080")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(GsonConverterFactory.create())
            .client(clent)
            .build()
    }

    val apiService: ApiService = HttpUtils.retrofit.create(ApiService::class.java)
}
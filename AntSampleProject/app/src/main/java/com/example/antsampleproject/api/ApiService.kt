package com.example.antsampleproject.api

import com.example.antsampleproject.data.model.TestApiModel
import com.example.antsampleproject.util.DefineConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {

        fun getInstance(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val gson = GsonBuilder() .setLenient() .create()

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(DefineConfig.DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }


    /**
     * 인트로 API
     * **/
    @POST("/test/getAndroid")
    suspend fun getTestApi(): Response<TestApiModel>

    /**
     * 로그인 API
     * **/
    @FormUrlEncoded
    @POST("/test/getAndroid")
    suspend fun setTestApi(@Field("id") market : String, @Field("pw") deviceWidth : String): Response<TestApiModel>
}
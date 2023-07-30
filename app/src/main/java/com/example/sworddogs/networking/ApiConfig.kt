package com.example.sworddogs.networking

import com.example.sworddogs.model.BreedResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        const val API_KEY = "live_SSG92GGnK5vQy6ezkjH6gguK2PtQ3xL0tsClj5i4PKPacbb2SPuLenvjOUpFr6Sp"

        fun getApiService(): ApiService {

            //api response interceptor
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            //client
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            //retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.thedogapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
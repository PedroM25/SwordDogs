package com.example.sworddogs.networking

import com.example.sworddogs.ListOfBreeds
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Get all breeds
    @GET("breeds")
    fun getAllBreeds(
        @Query("x-api-key") key: String = ApiConfig.API_KEY
    ): Call<ListOfBreeds>

    @GET("breeds")
    fun getLimitedBreeds(
        @Query("x-api-key") key: String = ApiConfig.API_KEY,
        @Query("limit") limitKey: Int,
        @Query("page") pageKey: Int
    ): Call<ListOfBreeds>
}


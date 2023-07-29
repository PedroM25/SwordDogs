package com.example.sworddogs.networking

import com.example.sworddogs.ListAllBreeds
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Get all breeds
    @GET("breeds")
    fun getAllBreeds(
        @Query("x-api-key") key: String = ApiConfig.API_KEY
    ): Call<ListAllBreeds>
}


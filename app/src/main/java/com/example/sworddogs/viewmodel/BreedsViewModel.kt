package com.example.sworddogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sworddogs.ListAllBreeds
import com.example.sworddogs.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedsViewModel : ViewModel() {

    private val _allBreedsData = MutableLiveData<ListAllBreeds?>()
    val allBreedsData: LiveData<ListAllBreeds?> get() = _allBreedsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getAllBreedsData(){
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getAllBreeds()

        // Send API request using Retrofit
        client.enqueue(object : Callback<ListAllBreeds> {
            override fun onResponse(
                call: Call<ListAllBreeds>,
                response: Response<ListAllBreeds>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _allBreedsData.postValue(responseBody)
            }

            override fun onFailure(call: Call<ListAllBreeds>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMessage: String?) {

        val message = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}
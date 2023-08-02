package com.example.sworddogs.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sworddogs.ListOfBreeds
import com.example.sworddogs.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedsViewModel : ViewModel() {

    private val _limitedBreedsData = MutableLiveData<ListOfBreeds>()
    val limitedBreedsData: LiveData<ListOfBreeds> get() = _limitedBreedsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean> get() = _isDone

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private var numberOfPageToRetrieve: Int = 0

    var errorMessage: String = ""
        private set

    fun getBreedsData(numberBreedsToLoad: Int){
        Log.i("PEDRO", "Entering method that calls API")
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getLimitedBreeds(limitKey=numberBreedsToLoad, pageKey = numberOfPageToRetrieve)

        // Send API request using Retrofit
        client.enqueue(object : Callback<ListOfBreeds> {
            override fun onResponse(
                call: Call<ListOfBreeds>,
                response: Response<ListOfBreeds>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                Log.i("PEDRO", "Response body was: $responseBody")
                if (responseBody.isEmpty()){
                    _isDone.value = true
                    return
                }
                numberOfPageToRetrieve++
                _isLoading.value = false
                _limitedBreedsData.postValue(responseBody!!) //never null because of previous check
            }

            override fun onFailure(call: Call<ListOfBreeds>, t: Throwable) {
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
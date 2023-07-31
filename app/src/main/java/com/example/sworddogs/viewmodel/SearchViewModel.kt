package com.example.sworddogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sworddogs.ListOfBreeds
import com.example.sworddogs.model.BreedResponse
import com.example.sworddogs.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList

class SearchViewModel : ViewModel() {

    private var _allBreedsData = LinkedList<BreedResponse>()

    private val _relevantBreedsFromSearchInput = MutableLiveData<ListOfBreeds>()
    val relevantBreedsFromSearchInput: LiveData<ListOfBreeds> get() = _relevantBreedsFromSearchInput

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getRelevantBreedsFromSearchInput() {
        _isLoading.value = true
        _isError.value = false
        if (_allBreedsData.isEmpty()){
            getAllBreedsData()
        }
        //...
        _isLoading.value = false
    }

    private fun getAllBreedsData() {
        val client = ApiConfig.getApiService().getAllBreeds()

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

                _allBreedsData = (responseBody as LinkedList<BreedResponse>)!! //never null because of previous check
            }

            override fun onFailure(call: Call<ListOfBreeds>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMessage: String?) {
        val message =
            if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
            else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}

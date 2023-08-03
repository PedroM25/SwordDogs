package com.example.sworddogs.viewmodel

import android.util.Log
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

class SearchBreedsViewModel : ViewModel() {

    private var _allBreedsData = ArrayList<BreedResponse>()

    private val _relevantBreedsFromSearchInput = MutableLiveData<ListOfBreeds>()
    val relevantBreedsFromSearchInput: LiveData<ListOfBreeds> get() = _relevantBreedsFromSearchInput

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    private val CLASS_TAG get() = this::class.simpleName

    fun getRelevantBreedsFromSearchInput(searchInput : String) {
        Log.i(CLASS_TAG, "Entered ViewModel method to get relevant breeds")
        _isLoading.value = true
        _isError.value = false
        if (_allBreedsData.isEmpty()){
            Log.i(CLASS_TAG, "Getting all breeds bc is empty")
            getAndSearchAllBreedsData(searchInput)
        } else {
            searchAllBreedsData(searchInput)
        }
    }

    private fun getAndSearchAllBreedsData(searchInput : String) {
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
                Log.d(CLASS_TAG, "response.body(): $responseBody")
                _allBreedsData = (responseBody as ArrayList<BreedResponse>)!! //never null because of previous check
                Log.d(CLASS_TAG, "_allBreedsData after: $_allBreedsData")
                searchAllBreedsData(searchInput)
            }

            override fun onFailure(call: Call<ListOfBreeds>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun searchAllBreedsData(searchInput: String) {
        _relevantBreedsFromSearchInput.postValue(_allBreedsData.breedResponseWhereStringIsSubstringOfName(searchInput))
        _isLoading.value = false
    }

    private fun onError(inputMessage: String?) {
        val message =
            if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
            else inputMessage

        errorMessage = message!! //checked above for null-ity

        Log.d(CLASS_TAG, "onError(): $message")

        _isError.value = true
        _isLoading.value = false
    }

    private fun ListOfBreeds.breedResponseWhereStringIsSubstringOfName(str: String) : ListOfBreeds{
        var relevantBreeds = LinkedList<BreedResponse>()
        for (item in this){
            if (item.name == null){
                continue
            }
            if (item.name.contains(str,true)){
                relevantBreeds.add(item)
            }
        }

        Log.i(CLASS_TAG, "Relevant breeds are: $relevantBreeds")
        return relevantBreeds
    }
}

package com.example.sworddogs.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BreedsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "All the breeds go here"
    }
    val text: LiveData<String> = _text
}
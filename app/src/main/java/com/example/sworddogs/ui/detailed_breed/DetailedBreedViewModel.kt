package com.example.sworddogs.ui.detailed_breed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailedBreedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "A detailed breed here"
    }
    val text: LiveData<String> = _text
}
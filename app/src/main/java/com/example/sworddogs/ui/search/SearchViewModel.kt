package com.example.sworddogs.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Where to search for breeds"
    }
    val text: LiveData<String> = _text
}
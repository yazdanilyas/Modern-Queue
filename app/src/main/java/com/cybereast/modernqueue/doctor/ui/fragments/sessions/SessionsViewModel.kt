package com.cybereast.modernqueue.doctor.ui.fragments.sessions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SessionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Sessions screen"
    }
    val text: LiveData<String> = _text
}
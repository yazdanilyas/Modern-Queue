package com.cybereast.virtualqueue.listeners

import androidx.appcompat.widget.AppCompatEditText

interface ValidationListener {
    fun onError(editText: AppCompatEditText)
    fun onSuccess()
}
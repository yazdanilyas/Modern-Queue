package com.cybereast.modernqueue.utils

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R

object InputValidation {


    fun isValidPhone(context: Context, editText: EditText): Boolean {
        val etValue = editText.text.toString().trim()
        return if (etValue.length < 10) {
            editText.error = context.getString(R.string.error_phone)
            false
        } else {
            true
        }
    }

    fun isValidEmail(context: Context, editText: EditText): Boolean {
        val etValue = editText.text.toString().trim()
        return if (Patterns.EMAIL_ADDRESS.matcher(etValue)
                .matches()
        ) {
            true
        } else {
            editText.error = context.getString(R.string.error_email)
            false
        }
    }


    fun isValidPassword(context: Context, editText: AppCompatEditText): Boolean {
        val value: String = editText.text.toString().trim()
        return if (value.length < 6) {
            editText.error = context.getString(R.string.error_password)
            false
        } else {
            true
        }

    }


}
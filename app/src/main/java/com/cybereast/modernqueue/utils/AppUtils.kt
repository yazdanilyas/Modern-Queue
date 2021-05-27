package com.cybereast.modernqueue.utils

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.application.MyApplication

object AppUtils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showHideProgressBar(progressBar: ProgressBar, hideShow: Int) {
        progressBar.visibility = hideShow
    }

    fun clearInputs(vararg editText: AppCompatEditText) {
        for (et in editText) {
            et.text?.clear()
        }
    }


    fun loadImageWithGide(imageContainer: ImageView, url: String) {
        val circularProgressDrawable = imageContainer.context?.let { CircularProgressDrawable(it) }
        circularProgressDrawable?.strokeWidth = 5f
        circularProgressDrawable?.centerRadius = 30f
        circularProgressDrawable?.start()
        Glide.with(MyApplication.getAppContext())
            .load(url)
            .error(R.drawable.ic_broken_image)
            .placeholder(circularProgressDrawable)
            .into(imageContainer)
    }

}
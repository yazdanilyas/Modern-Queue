package com.cybereast.modernqueue.doctor.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants.SPLASH_TIME_OUT
import com.cybereast.modernqueue.utils.ActivityUtils

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openChooserActivity()
    }

    private fun openChooserActivity() {
        val mRunnable = Runnable {
            if (!isFinishing) {
                ActivityUtils.startActivity(this, ChooserActivity::class.java)
                finish()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(mRunnable, SPLASH_TIME_OUT)
    }

}

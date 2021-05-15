package com.cybereast.virtualqueue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cybereast.virtualqueue.databinding.ActivityMainBinding
import com.cybereast.virtualqueue.doctor.ui.activities.DashboardActivity
import com.cybereast.virtualqueue.doctor.ui.activities.LoginActivity
import com.cybereast.virtualqueue.doctor.ui.activities.SignUpActivity
import com.cybereast.virtualqueue.utils.AppUtils

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    private fun setListeners() {
        mBinding.doctorCardView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        mBinding.patientCardView.setOnClickListener {
            AppUtils.showToast(applicationContext, "Under developing")
        }
    }
}
package com.cybereast.virtualqueue.doctor.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cybereast.virtualqueue.R
import com.cybereast.virtualqueue.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    private fun setListeners() {
        mBinding.loginBtn.setOnClickListener {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
        }
        mBinding.signUpTv.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }
    }
}
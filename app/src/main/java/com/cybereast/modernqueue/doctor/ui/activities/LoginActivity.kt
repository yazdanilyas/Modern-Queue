package com.cybereast.modernqueue.doctor.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.ActivityLoginBinding
import com.cybereast.modernqueue.listeners.ValidationListener
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.InputValidation
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), ValidationListener {
    private lateinit var mBinding: ActivityLoginBinding
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mFirebaseAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (InputValidation.isValidEmail(this, mBinding.userEmailEt)) {
            login()
        }
    }

    private fun login() {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        mAuth.signInWithEmailAndPassword(
            mBinding.userEmailEt.text.toString(),
            mBinding.userPasswordEt.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                ActivityUtils.startActivity(this, DashboardActivity::class.java)
                finish()
            } else {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(this, "Invalid email or password")
            }
        }
    }

    private fun setListeners() {
        mBinding.loginBtn.setOnClickListener {
            validateFields(mBinding.userEmailEt, mBinding.userPasswordEt)
        }
        mBinding.signUpTv.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }
        setAuthenticationListener()

    }


    private fun validateFields(vararg editText: AppCompatEditText) {
        var isValid = true
        for (et in editText) {
            if (TextUtils.isEmpty(et.text)) {
                isValid = false
                onError(et)
            }
        }
        if (isValid) {
            onSuccess()
        }
    }

    private fun setAuthenticationListener() {
        mFirebaseAuthListener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser
            if (currentUser != null) {
                ActivityUtils.startActivity(this, DashboardActivity::class.java)
            } else {
                ActivityUtils.startActivity(this, LoginActivity::class.java)
            }

        }
    }

}
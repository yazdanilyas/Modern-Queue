package com.cybereast.virtualqueue.doctor.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.virtualqueue.R
import com.cybereast.virtualqueue.databinding.ActivitySignUpBinding
import com.cybereast.virtualqueue.listeners.ValidationListener
import com.cybereast.virtualqueue.utils.AppUtils.showToast
import com.cybereast.virtualqueue.utils.InputValidation
import com.cybereast.virtualqueue.utils.InputValidation.isValidEmail
import com.cybereast.virtualqueue.utils.InputValidation.isValidPhone

class SignUpActivity : AppCompatActivity(), ValidationListener {
    private lateinit var mBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (isValidEmail(applicationContext, mBinding.emailEt) && isValidPhone(
                applicationContext,
                mBinding.mobileNoEt
            )
        ) {

            signUp()
        }
    }

    private fun signUp() {
        showToast(applicationContext, "success")
//        startActivity(Intent(applicationContext, DashboardActivity::class.java))
    }

    private fun setListeners() {
        mBinding.signUpButton.setOnClickListener {
            validateFields(
                mBinding.firstNameEt,
                mBinding.lastNameEt,
                mBinding.emailEt,
                mBinding.mobileNoEt,
                mBinding.hospitalNameEt,
                mBinding.countryNameEt,
                mBinding.stateEt,
                mBinding.districtEt
            )
        }
        mBinding.loginTv.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
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


}
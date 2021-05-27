package com.cybereast.modernqueue.doctor.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.constants.Constants.COLLECTION_DOCTORS
import com.cybereast.modernqueue.databinding.ActivitySignUpBinding
import com.cybereast.modernqueue.listeners.ValidationListener
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils.showHideProgressBar
import com.cybereast.modernqueue.utils.AppUtils.showToast
import com.cybereast.modernqueue.utils.InputValidation.isValidEmail
import com.cybereast.modernqueue.utils.InputValidation.isValidPassword
import com.cybereast.modernqueue.utils.InputValidation.isValidPhone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity(), ValidationListener {
    private lateinit var mBinding: ActivitySignUpBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private lateinit var doctorDocumentRef: DocumentReference
    private val mAuth = FirebaseAuth.getInstance()

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
        if (isValidEmail(applicationContext, mBinding.emailEt)
            && isValidPhone(
                applicationContext,
                mBinding.mobileNoEt
            )
            && isValidPassword(applicationContext, mBinding.passwordEt)
        ) {

            signUp()
        }
    }

    private fun signUp() {
        showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        val doctor = Doctor(
            mBinding.firstNameEt.text.toString(),
            mBinding.lastNameEt.text.toString(),
            mBinding.emailEt.text.toString(),
            mBinding.mobileNoEt.text.toString(),
            mBinding.passwordEt.text.toString(),
            mBinding.hospitalNameEt.text.toString(),
            mBinding.countryNameEt.text.toString(),
            mBinding.stateEt.text.toString(),
            mBinding.districtEt.text.toString(),
            mBinding.specialityEt.text.toString(),
            Integer.parseInt(mBinding.consultFeeEt.text.toString()),
            false
        )
        mAuth.createUserWithEmailAndPassword(doctor.email.toString(), doctor.password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uId = it.result?.user?.uid
                    if (uId != null) {

                        doctorDocumentRef =
                            fireStoreDbRef.collection(COLLECTION_DOCTORS).document(uId).collection(
                                Constants.COLLECTION_PROFILE
                            ).document()
                        doctorDocumentRef.set(doctor).addOnSuccessListener {
                            showHideProgressBar(mBinding.progressBar, View.GONE)
                            ActivityUtils.startActivity(this, DashboardActivity::class.java)
                            showToast(
                                applicationContext,
                                getString(R.string.sign_up_success_message)
                            )
                        }.addOnFailureListener {
                            showHideProgressBar(mBinding.progressBar, View.GONE)
                            showToast(applicationContext, it.message.toString())
                            Log.d("TAG", "signUp: ${it.message}")
                        }
                    }
                } else {
                    showHideProgressBar(mBinding.progressBar, View.GONE)
                    showToast(applicationContext, it.exception?.message.toString())
                    Log.d("TAG", it.exception?.message.toString())
                }
            }

    }

    private fun setListeners() {
        mBinding.signUpButton.setOnClickListener {
            validateFields(
                mBinding.firstNameEt,
                mBinding.lastNameEt,
                mBinding.emailEt,
                mBinding.mobileNoEt,
                mBinding.passwordEt,
                mBinding.hospitalNameEt,
                mBinding.countryNameEt,
                mBinding.stateEt,
                mBinding.districtEt,
                mBinding.specialityEt,
                mBinding.consultFeeEt,
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
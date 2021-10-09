package com.cybereast.modernqueue.doctor.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.ActivityEditProfileBinding
import com.cybereast.modernqueue.listeners.ValidationListener
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.InputValidation
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity(), ValidationListener {
    private lateinit var mBinding: ActivityEditProfileBinding
    private var uid: String? = null
    private var fireStoreDb = FirebaseFirestore.getInstance()
    private lateinit var documentRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        getExtra()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.update_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_profile -> {
                validateFields(
                    mBinding.firstNameEt,
                    mBinding.lastNameEt,
                    mBinding.mobileNoEt,
                    mBinding.hospitalNameEt,
                    mBinding.countryNameEt,
                    mBinding.stateEt,
                    mBinding.districtEt,
                    mBinding.specialityEt,
                    mBinding.consultFeeEt,
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        if (InputValidation.isValidPhone(
                applicationContext,
                mBinding.mobileNoEt
            )
        ) {
            updateProfile()

        }
    }

    private fun updateProfile() {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        if (uid != null) {
            val doctor = Doctor()
            val userMap = hashMapOf<String, Any>(
                "firstName" to mBinding.firstNameEt.text.toString(),
                "lastName" to mBinding.lastNameEt.text.toString(),
                "mobile" to mBinding.mobileNoEt.text.toString(),
                "hospitalName" to mBinding.hospitalNameEt.text.toString(),
                "country" to mBinding.countryNameEt.text.toString(),
                "state" to mBinding.stateEt.text.toString(),
                "district" to mBinding.districtEt.text.toString(),
                "speciality" to mBinding.specialityEt.text.toString()

            )
            fireStoreDb.collection(Constants.COLLECTION_DOCTORS).document(uid.toString())
                .collection(Constants.COLLECTION_PROFILE).get()
                .addOnSuccessListener {
                    val docRefId = it.documents.get(0).id

                    documentRef = fireStoreDb.collection(Constants.COLLECTION_DOCTORS)
                        .document(uid.toString()).collection(Constants.COLLECTION_PROFILE)
                        .document(docRefId)
                    documentRef.update(userMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                            AppUtils.showToast(this, "User updated successfully")
                            finish()
                        }
                    }.addOnFailureListener {
                        AppUtils.showToast(this, it.message.toString())
                        AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                    }


                }.addOnFailureListener {
                    Log.d("TAG", "updateProfile: " + it.message)
                }


        } else {
            AppUtils.showToast(this, getString(R.string.session_expired))
            ActivityUtils.startActivity(this, LoginActivity::class.java)
            finish()
        }
    }


    private fun getExtra() {
        uid = intent.extras?.getString(Constants.KEY_UID)
        Log.d("TAG", "getExtra: " + uid)
        getUserProfileData(uid)
    }


    private fun getUserProfileData(puid: String?) {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        if (puid != null) {
            fireStoreDb.collection(Constants.COLLECTION_DOCTORS).document(puid.toString())
                .collection(Constants.COLLECTION_PROFILE).get().addOnSuccessListener {
                    val currentUser = it.toObjects(Doctor::class.java).get(0)
                    setProfileData(currentUser)
                    AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)

                }.addOnFailureListener {
                    AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                    AppUtils.showToast(this, it.message.toString())
                }
        } else {
            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            AppUtils.showToast(this, getString(R.string.session_expired))
            ActivityUtils.startActivity(this, LoginActivity::class.java)
            finish()
        }
    }

    private fun setProfileData(currentUser: Doctor?) {
        mBinding.firstNameEt.setText(currentUser?.firstName)
        mBinding.lastNameEt.setText(currentUser?.lastName)
        mBinding.mobileNoEt.setText(currentUser?.mobile)

        mBinding.hospitalNameEt.setText(currentUser?.hospitalName)
        mBinding.countryNameEt.setText(currentUser?.country)
        mBinding.stateEt.setText(currentUser?.state)
        mBinding.districtEt.setText(currentUser?.district)
        mBinding.specialityEt.setText(currentUser?.speciality)
        mBinding.consultFeeEt.setText(currentUser?.consultancyFee.toString())
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
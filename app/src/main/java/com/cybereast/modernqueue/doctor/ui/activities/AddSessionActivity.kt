package com.cybereast.modernqueue.doctor.ui.activities

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.ActivityAddSessionBinding
import com.cybereast.modernqueue.listeners.ValidationListener
import com.cybereast.modernqueue.models.Session
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.AppUtils.showHideProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddSessionActivity : AppCompatActivity(), ValidationListener {

    private lateinit var mBinding: ActivityAddSessionBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private lateinit var doctorDocumentRef: DocumentReference
    private val mAuth = FirebaseAuth.getInstance()
    var mUId = mAuth.currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddSessionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    override fun onError(editText: AppCompatEditText) {
        editText.error = getString(R.string.required_field)
    }

    override fun onSuccess() {
        val session = Session(
                null,
                mBinding.startTimeEt.text.toString(),
                mBinding.endTimeEt.text.toString(),
                Integer.parseInt(mBinding.noOfTokens.text.toString()),
                false
        )
        addNewSession(session)
    }

    private fun setListeners() {
        mBinding.addSessionBtn.setOnClickListener {
            validateFields(mBinding.startTimeEt, mBinding.endTimeEt, mBinding.noOfTokens)
        }
        mBinding.startTimeEt.setOnClickListener {
            val time = timePicker(this, true)
            mBinding.startTimeEt.setText(time)

        }
        mBinding.endTimeEt.setOnClickListener {
            val time = timePicker(this, false)
            mBinding.endTimeEt.setText(time)
        }
    }

    private fun addNewSession(session: Session) {
        showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        mUId = mAuth.currentUser?.uid
        if (mUId != null) {
            doctorDocumentRef =
                    fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(mUId.toString())
                            .collection(
                                    Constants.COLLECTION_SESSIONS
                            ).document()

            doctorDocumentRef.set(session).addOnSuccessListener {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(this, getString(R.string.session_added))
                onBackPressed()

            }.addOnFailureListener {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(this, it.message.toString())
                Log.d("TAG", "addNewSession: ${it.message}")
            }

        } else {
//            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            AppUtils.showToast(this, getString(R.string.session_expired))
            ActivityUtils.startActivity(this, LoginActivity::class.java)
            finish()
        }

    }

    private fun timePicker(activity: Activity, isStart: Boolean): String? {
        var time: String? = null
        val c: Calendar = Calendar.getInstance()
        var mHour = c.get(Calendar.HOUR_OF_DAY)
        var mMinute = c.get(Calendar.MINUTE)

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
                activity, { view, hourOfDay, minute ->
            time = setTime12Hour(hourOfDay, minute)
            if (isStart) {
                mBinding.startTimeEt.setText(time)
            } else {

                mBinding.endTimeEt.setText(time)
            }
        },
                mHour,
                mMinute,
                false
        )
        timePickerDialog.show()
        return time
    }

    private fun setTime12Hour(hoursOfDay: Int, minute: Int): String {
        var time: String? = null
        var amPm: String? = null
        var hourOfDay = hoursOfDay
        var AM_PM = "AM"
        var minPrecede = ""
        if (hourOfDay >= 12) {
            AM_PM = "PM"
            if (hourOfDay >= 13 && hourOfDay < 24) {
                hourOfDay -= 12
            } else {
                hourOfDay = 12
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12
        }

        if (minute < 10) {
            minPrecede = "0"
        }
        amPm = AM_PM
        time = "$hourOfDay:$minPrecede$minute $amPm"
        return time
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
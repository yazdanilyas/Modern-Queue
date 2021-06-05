package com.cybereast.modernqueue.doctor.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cybereast.modernqueue.databinding.ActivityMainBinding
import com.cybereast.modernqueue.patient.activities.PatientSigninActivity
import com.cybereast.modernqueue.utils.ActivityUtils
import com.google.firebase.auth.FirebaseAuth

class ChooserActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setListeners()
    }

    private fun setListeners() {
        mBinding.doctorCardView.setOnClickListener {
            checkUserSession()
        }
        mBinding.patientCardView.setOnClickListener {
            ActivityUtils.startActivity(this, PatientSigninActivity::class.java)
        }
    }

    private fun checkUserSession() {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val bundle = Bundle()
            bundle.putString("KEY_UID", currentUser.uid)
            ActivityUtils.startActivity(this, DashboardActivity::class.java, bundle)
        } else {
            ActivityUtils.startActivity(this, LoginActivity::class.java)
        }
    }


}
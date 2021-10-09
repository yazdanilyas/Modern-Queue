package com.cybereast.modernqueue.doctor.ui.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentProfileBinding
import com.cybereast.modernqueue.doctor.ui.activities.EditProfileActivity
import com.cybereast.modernqueue.doctor.ui.activities.LoginActivity
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var mBinding: FragmentProfileBinding
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser
    private var fireStoreDb = FirebaseFirestore.getInstance()
    private var uid: String? = null
    private lateinit var documentRef: DocumentReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        setListeners()
        return mBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        mBinding.doctorProfileHeaderLayout.editTv.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra(Constants.KEY_UID, uid)
            startActivity(intent)
        }
        mBinding.availabilitySwitch.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    mBinding.availabilitySwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
                        setAvailabilityStatus(isChecked)

                    }
                    mBinding.availabilitySwitch.performClick()
                    true
                }

                else -> false
            }
        }


    }

    private fun setAvailabilityStatus(checked: Boolean) {
        if (uid != null) {
            val doctor = Doctor()
            val userMap = hashMapOf<String, Any>(
                "available" to checked
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
                            AppUtils.showToast(requireContext(), "Availability changed")
                        }
                    }.addOnFailureListener {
                        AppUtils.showToast(requireContext(), it.message.toString())
                        AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                    }


                }.addOnFailureListener {
                    Log.d("TAG", "updateProfile: " + it.message)
                }


        } else {
            AppUtils.showToast(requireContext(), getString(R.string.session_expired))
            ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserProfileData()
    }

    private fun getUserProfileData() {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        uid = currentUser?.uid
        if (uid != null) {
            fireStoreDb.collection(Constants.COLLECTION_DOCTORS).document(uid.toString())
                .collection(Constants.COLLECTION_PROFILE).get().addOnSuccessListener {
                    if (!it.isEmpty) {
                        val currentUser = it.toObjects(Doctor::class.java)
                        setProfileData(currentUser[0])
                        AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                    } else {
                        AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                        AppUtils.showToast(requireContext(), getString(R.string.session_expired))
                        ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
                        activity?.finish()
                    }

                }.addOnFailureListener {
                    AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
                    AppUtils.showToast(requireContext(), it.message.toString())
                }
        } else {
            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            AppUtils.showToast(requireContext(), getString(R.string.session_expired))
            ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
            activity?.finish()
        }
    }

    private fun setProfileData(currentUser: Doctor?) {
        mBinding.doctorProfileHeaderLayout.userNameTv.text =
            "${currentUser?.firstName} ${currentUser?.lastName}"
        mBinding.personalLayoutDoctorProfile.dEmailTv.text = currentUser?.email
        mBinding.personalLayoutDoctorProfile.dMobileTv.text = currentUser?.mobile
        mBinding.personalLayoutDoctorProfile.dSpecialityTv.text = currentUser?.speciality
        mBinding.hospitalLayoutDoctorProfile.dhospitalNamelTv.text = currentUser?.hospitalName
        mBinding.hospitalLayoutDoctorProfile.dCountryTv.text = currentUser?.country
        mBinding.hospitalLayoutDoctorProfile.dStateTv.text = currentUser?.state
        mBinding.hospitalLayoutDoctorProfile.dDistrictTv.text = currentUser?.district
        mBinding.hospitalLayoutDoctorProfile.dConsultancyFeeTv.text =
            currentUser?.consultancyFee.toString()
        mBinding.availabilitySwitch.isChecked = currentUser?.available ?: false
    }
}
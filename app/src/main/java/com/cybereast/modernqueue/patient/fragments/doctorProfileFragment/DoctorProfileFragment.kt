package com.cybereast.modernqueue.patient.fragments.doctorProfileFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.base.BaseInterface
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.DoctorProfileFragmentBinding
import com.cybereast.modernqueue.doctor.ui.activities.LoginActivity
import com.cybereast.modernqueue.doctor.ui.fragments.sessions.DoctorsSessionsFragment
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.CommonKeys
import com.google.firebase.firestore.FirebaseFirestore

class DoctorProfileFragment : Fragment(), BaseInterface {

    companion object {
        fun newInstance() = DoctorProfileFragment()
    }

    private lateinit var mBinding: DoctorProfileFragmentBinding
    private lateinit var viewModel: DoctorProfileViewModel
    private var fireStoreDb = FirebaseFirestore.getInstance()
    private var uid: String? = null
    private lateinit var doctor: Doctor
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mBinding = DoctorProfileFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorProfileViewModel::class.java)
        getUserProfileData()
        setListeners()
    }

    private fun setListeners() {
        mBinding.sessionCv.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.KEY_DATA,doctor)
//            bundle.putString(CommonKeys.KEY_UID, uid)
            ActivityUtils.launchFragment(
                    requireContext(),
                    DoctorsSessionsFragment::class.java.name,
                    bundle
            )
        }
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun getUserProfileData() {
        showProgressBar()
        uid = getUId()
        if (uid != null) {
            fireStoreDb.collection(Constants.COLLECTION_DOCTORS).document(uid.toString())
                    .collection(Constants.COLLECTION_PROFILE).get().addOnSuccessListener {
                        if (!it.isEmpty) {
                            val currentUser = it.toObjects(Doctor::class.java)
                            doctor=currentUser[0]
                            setProfileData(doctor)
                            hideProgressBar()
                            mBinding.parentView.visibility = View.VISIBLE
                            mBinding.noDataTv.visibility = View.GONE
                        } else {
                            hideProgressBar()
                            AppUtils.showToast(requireContext(), getString(R.string.session_expired))
                            ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
                            activity?.finish()
                        }

                    }.addOnFailureListener {
                        hideProgressBar()
                        AppUtils.showToast(requireContext(), it.message.toString())
                    }
        } else {
            hideProgressBar()
            mBinding.parentView.visibility = View.GONE
            mBinding.noDataTv.visibility = View.VISIBLE
        }
    }

    private fun getUId(): String? {
        val uid = arguments?.getString(CommonKeys.KEY_UID)
        Log.d("TAG", "getUId: $uid")
        return uid
    }

    private fun setProfileData(currentUser: Doctor?) {
        mBinding.userNameTv.text =
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
        if (currentUser?.available == true) {
            mBinding.availabilityTv.text = getString(R.string.available)
        } else {
            mBinding.availabilityTv.text = getString(R.string.not_available_today)
        }
    }


}
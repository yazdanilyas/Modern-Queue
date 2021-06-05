package com.cybereast.modernqueue.patient.fragments.moreFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.FragmentMorePBinding
import com.cybereast.modernqueue.doctor.ui.activities.LoginActivity
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.google.firebase.auth.FirebaseAuth

class MoreFragmentP : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MoreFragmentP()
        fun newInstance(pBundle: Bundle) = MoreFragmentP().apply {
            arguments = pBundle
        }
    }

    private lateinit var mBinding: FragmentMorePBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMorePBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        mBinding.shareAppCv.setOnClickListener(this)
        mBinding.rateAppCv.setOnClickListener(this)
        mBinding.logoutCv.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.shareAppCv -> {
                AppUtils.shareApp(requireActivity())
            }
            R.id.rateAppCv -> {
                AppUtils.rateApp(requireActivity())
            }
            R.id.logoutCv -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        ActivityUtils.startActivity(requireActivity(), LoginActivity::class.java)
        activity?.finish()
    }


}
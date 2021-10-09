package com.cybereast.modernqueue.doctor.ui.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.FragmentMoreBinding
import com.cybereast.modernqueue.doctor.ui.activities.LoginActivity
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.google.firebase.auth.FirebaseAuth

class MoreFragment : Fragment(), View.OnClickListener {

    private lateinit var moreViewModel: MoreViewModel
    private lateinit var mBinding: FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        moreViewModel =
            ViewModelProvider(this).get(MoreViewModel::class.java)
        mBinding = FragmentMoreBinding.inflate(inflater, container, false)
        setListeners()
        return mBinding.root
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
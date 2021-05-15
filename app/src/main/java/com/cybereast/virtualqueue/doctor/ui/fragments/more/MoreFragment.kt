package com.cybereast.virtualqueue.doctor.ui.fragments.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.virtualqueue.R
import com.cybereast.virtualqueue.databinding.FragmentMoreBinding

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
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.shareAppCv -> {
                shareApp()
            }
            R.id.rateAppCv -> {
                rateApp()
            }
        }
    }

    private fun rateApp() {
        val rateIntent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + requireActivity().packageName)
            )
        startActivity(rateIntent)
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_app_message) + "https://play.google.com/store/apps/details?id=" + requireActivity().packageName
        )
        startActivity(shareIntent)

    }
}
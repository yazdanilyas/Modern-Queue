package com.cybereast.modernqueue.patient.fragments.bookingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.databinding.FragmentBookingPBinding


class BookingFragmentP : Fragment() {

    companion object {
        fun newInstance() = BookingFragmentP()
        fun newInstance(mBundle: Bundle?) = BookingFragmentP().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentBookingPBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBookingPBinding.inflate(inflater, container, false)
        return mBinding.root
    }

}
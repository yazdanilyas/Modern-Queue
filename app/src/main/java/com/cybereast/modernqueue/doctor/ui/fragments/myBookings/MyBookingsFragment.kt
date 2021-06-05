package com.cybereast.modernqueue.doctor.ui.fragments.myBookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.R

class MyBookingsFragment : Fragment() {
    companion object {
        fun newInstance() = MyBookingsFragment()
        fun newInstance(mBundle: Bundle) = MyBookingsFragment().apply {
            arguments = mBundle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false)
    }
}

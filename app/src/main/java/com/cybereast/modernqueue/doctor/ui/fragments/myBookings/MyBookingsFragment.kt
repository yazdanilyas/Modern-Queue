package com.cybereast.modernqueue.doctor.ui.fragments.myBookings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentMyBookingsBinding
import com.cybereast.modernqueue.enums.BookingStatus
import com.cybereast.modernqueue.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyBookingsFragment : Fragment() {
    companion object {
        fun newInstance() = MyBookingsFragment()
        fun newInstance(mBundle: Bundle) = MyBookingsFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentMyBookingsBinding
    private lateinit var mViewModel: MyBookingViewModel
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MyBookingViewModel::class.java)
        getMyBookings()

    }

    private fun getMyBookings() {
        val query =
                fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
                        .whereEqualTo("uid", mAuth.currentUser?.uid).get()
                        .addOnSuccessListener { snapshot ->

                            Log.d("TAG", "getMyBookings: " + snapshot.size())
                            for (snap in snapshot) {
                                val booking = snap.toObject(Booking::class.java)
                                Log.d("TAG", "getMyBookings: $booking")
                            }

                        }
    }
}

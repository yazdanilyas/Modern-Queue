package com.cybereast.modernqueue.patient.fragments.bookingDetailFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.modernqueue.adapters.BookingAdapter
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.BookingDetailFragmentBinding
import com.cybereast.modernqueue.models.Booking
import com.cybereast.modernqueue.utils.CommonKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingDetailFragmentP : Fragment() {

    companion object {
        fun newInstance() = BookingDetailFragmentP()
    }

    private lateinit var viewModel: BookingDetailFragmentViewModel
    private lateinit var mBinding: BookingDetailFragmentBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mAdapter: BookingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = BookingDetailFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookingDetailFragmentViewModel::class.java)
        getBundleData()
        getMyBookings()
    }

    private fun setUpRecycler() {
        mAdapter =
            BookingAdapter(viewModel.bookingList, null)
        mBinding.bookingRecycler.layoutManager =
            LinearLayoutManager(activity)
        mBinding.bookingRecycler.setHasFixedSize(true)
        mBinding.bookingRecycler.adapter = mAdapter
    }

    private fun getBundleData() {
        arguments.let {
            viewModel.sessionId = it?.getString(CommonKeys.KEY_SESSION_ID)
//            Log.d(TAG, "getBundleData: ")
        }
    }

    private fun getMyBookings() {
        val query =
            fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
                .whereEqualTo("sessionId", viewModel.sessionId).get()
                .addOnSuccessListener { snapshot ->

                    Log.d("TAG", "getMyBookings: " + snapshot.size())
                    for (snap in snapshot) {
                        val booking = snap.toObject(Booking::class.java)
                        viewModel.bookingList.add(booking)
                        Log.d("TAG", "getMyBookings: $booking")
                    }
                    setUpRecycler()
                }
    }

}
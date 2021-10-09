package com.cybereast.modernqueue.patient.fragments.bookingFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.adapters.BookingAdapterP
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentBookingPBinding
import com.cybereast.modernqueue.enums.BookingStatus
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Booking
import com.cybereast.modernqueue.patient.fragments.bookingDetailFragment.BookingDetailFragmentP
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.CommonKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class BookingFragmentP : Fragment() {

    companion object {
        fun newInstance() = BookingFragmentP()
        fun newInstance(mBundle: Bundle?) = BookingFragmentP().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentBookingPBinding
    private lateinit var mViewModel: BookingPViewModel
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mAdapter: BookingAdapterP
    private var mRecyclerListener = object : RecyclerItemClickListener {
        override fun onClick(data: Any?, position: Int) {
            val booking = data as Booking
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_SESSION_ID, booking.sessionId)
            ActivityUtils.launchFragment(
                requireContext(),
                BookingDetailFragmentP::class.java.name,
                bundle
            )
            Log.d("TAG", "onClick: ${booking.sessionId}")
        }

        override fun onItemChildClick(view: View, data: Any?) {
            val booking = data as Booking
            setPatientBookingStatus(view, booking)
        }

        override fun onSeeProfile(data: Any?, position: Int) {

        }

    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(BookingPViewModel::class.java)
        getMyBookings()
        setListeners()
    }

    private fun setListeners() {

    }

    private fun setUpRecycler() {
        mAdapter =
            BookingAdapterP(mViewModel.bookingList, mRecyclerListener)
        mBinding.bookingRecycler.layoutManager =
            LinearLayoutManager(activity)
        mBinding.bookingRecycler.setHasFixedSize(true)
        mBinding.bookingRecycler.adapter = mAdapter
    }

    private fun getMyBookings() {
        val query =
            fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
                .whereEqualTo("patientId", mAuth.currentUser?.uid).get()
                .addOnSuccessListener { snapshot ->

                    Log.d("TAG", "getMyBookings: " + snapshot.size())
                    for (snap in snapshot) {
                        val booking = snap.toObject(Booking::class.java)
                        mViewModel.bookingList.add(booking)
                        Log.d("TAG", "getMyBookings: $booking")
                    }
                    setUpRecycler()
                }
    }

    private fun setPatientBookingStatus(view: View, booking: Booking) {
        val popUp = PopupMenu(requireActivity(), view)
        popUp.menuInflater.inflate(R.menu.menu_booking_options_cancelled, popUp.menu)
        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_cancelled -> {
                    setBookingStatus(view, booking, BookingStatus.CANCELLED.toString())
                }
            }
            true
        }
        popUp.show()
    }

    private fun setBookingStatus(view: View, booking: Booking, status: String) {
//        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        val userMap = hashMapOf<String, Any>(
            "bookingStatus" to status
        )
        val docRef = fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
            .document(booking.bookingId.toString())
        docRef.update(userMap).addOnSuccessListener {
            AppUtils.showToast(requireContext(), "Status Updated")

        }.addOnFailureListener {
            AppUtils.showToast(requireContext(), it.message.toString())
        }
    }
}
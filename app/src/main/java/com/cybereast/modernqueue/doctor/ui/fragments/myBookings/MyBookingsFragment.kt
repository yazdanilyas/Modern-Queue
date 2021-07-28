package com.cybereast.modernqueue.doctor.ui.fragments.myBookings

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
import com.cybereast.modernqueue.adapters.BookingAdapter
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentMyBookingsBinding
import com.cybereast.modernqueue.enums.BookingStatus
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Booking
import com.cybereast.modernqueue.models.Session
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.CommonKeys
import com.google.android.gms.tasks.OnSuccessListener
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
    private lateinit var mAdapter: BookingAdapter

    private var mRecyclerListener = object : RecyclerItemClickListener {
        override fun onClick(data: Any?, position: Int) {
            val session = data as Session
            val bundle = Bundle()
            Log.d("TAG", "onClick: ${session.sessionId}")
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
    ): View? {
        mBinding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MyBookingViewModel::class.java)
        getBundleData()
        getMyBookings()
        setListeners()
    }

    private fun setListeners() {
        mBinding.endSessionButton.setOnClickListener {
            val batchWrite = fireStoreDbRef.batch()
            for (booking in mViewModel.bookingList) {
                val docRef = fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
                    .document(booking.bookingId.toString())
                batchWrite.delete(docRef)
            }
            batchWrite.commit().addOnSuccessListener(OnSuccessListener<Void?> {
                AppUtils.showToast(requireContext(), "Session ended")
            }).addOnFailureListener {
                AppUtils.showToast(requireContext(), it.message.toString())
            }
        }
    }

    private fun setUpRecycler() {
        mAdapter =
            BookingAdapter(mViewModel.bookingList, mRecyclerListener)
        mBinding.bookingRecycler.layoutManager =
            LinearLayoutManager(activity)
        mBinding.bookingRecycler.setHasFixedSize(true)
        mBinding.bookingRecycler.adapter = mAdapter
    }

    private fun getBundleData() {
        arguments.let {
            mViewModel.sessionId = it?.getString(CommonKeys.KEY_SESSION_ID)
//            Log.d(TAG, "getBundleData: ")
        }
    }

    private fun getMyBookings() {
        val query =
            fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING)
                .whereEqualTo("uid", mAuth.currentUser?.uid)
                .whereEqualTo("sessionId", mViewModel.sessionId).get()
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
        popUp.menuInflater.inflate(R.menu.menu_booking_options, popUp.menu)
        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_consulting -> {
                    setBookingStatus(view, booking, BookingStatus.CONSULTING.toString())
                }
                R.id.menu_consulted -> {
                    setBookingStatus(view, booking, BookingStatus.CONSULTED.toString())
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

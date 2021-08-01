package com.cybereast.modernqueue.patient.fragments.sessions

import android.content.DialogInterface
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
import com.cybereast.modernqueue.adapters.SessionsAdapterPatient
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentSessionsDoctorBinding
import com.cybereast.modernqueue.doctor.ui.fragments.sessions.DoctorsSessionsViewModel
import com.cybereast.modernqueue.enums.BookingStatus
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.listeners.SwitchStateListener
import com.cybereast.modernqueue.models.Booking
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.models.Patient
import com.cybereast.modernqueue.models.Session
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.CommonKeys
import com.cybereast.modernqueue.utils.DialogUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class DoctorsSessionsFragment : Fragment() {

    private lateinit var doctorsSessionsViewModel: DoctorsSessionsViewModel
    private lateinit var doctorDocumentRef: DocumentReference
    private lateinit var mBinding: FragmentSessionsDoctorBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var mUId: String? = null
    private var patientUserId = mAuth.currentUser?.uid
    private var sessionList: ArrayList<Session> = ArrayList()
    private lateinit var mAdapter: SessionsAdapterPatient
    private lateinit var mViewModel: DoctorsSessionsViewModel
    private var mRecyclerListener = object : RecyclerItemClickListener {
        override fun onClick(data: Any?, position: Int) {

        }

        override fun onItemChildClick(view: View, data: Any?) {
//            val session = data as Session
//            deletePopUp(view, session.sessionId)
            mViewModel.mSession = data as Session
            var sessionBookingStatus: Boolean? = null
            mViewModel.mSession.let {
                sessionBookingStatus = it?.booking
            }
            val doctorAvailability = mViewModel.mDoctor?.available
            if (doctorAvailability == true) {
                if (sessionBookingStatus == true) {
                    activity?.let { DialogUtils.bookingDialog(it, mDialogButtonListener) }

                } else {
                    AppUtils.showToast(requireContext(), "Booking not opened yet for this session")
                }

            } else {
                AppUtils.showToast(requireContext(), "Doctor not available today")
            }

        }

        override fun onSeeProfile(data: Any?, position: Int) {

        }

    }

    private val mDialogButtonListener = object : DialogUtils.DialogButtonListener {
        override fun onPositiveClick(data: Any) {
            val patient = data as Patient
            saveBooking(mViewModel.mSession, mViewModel.mDoctor, patient)
        }

        override fun onNegativeClick(dialog: DialogInterface) {
            dialog.dismiss()
        }


    }

    private fun saveBooking(session: Session?, doctor: Doctor?, patient: Patient) {

        val booking = Booking(
            null,
            doctor?.uId,
            doctor?.firstName,
            doctor?.lastName,
            doctor?.email,
            doctor?.mobile,
            doctor?.hospitalName,
            doctor?.speciality,
            doctor?.consultancyFee,
            doctor?.available,
            session?.sessionId,
            session?.startTime,
            session?.endTime,
            session?.noOfTokens,
            session?.booking,
            BookingStatus.OPENED.toString(),
            patientUserId,
            patient.name,
            patient.phoneNumber
        )
        doctorDocumentRef =
            fireStoreDbRef.collection(Constants.COLLECTION_PATIENT_BOOKING).document()
        val bookingId = doctorDocumentRef.id
        booking.bookingId = bookingId
        doctorDocumentRef.set(booking).addOnSuccessListener {
            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            AppUtils.showToast(requireContext(), getString(R.string.my_booking))

        }.addOnFailureListener {
            AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            AppUtils.showToast(requireContext(), it.message.toString())
            Log.d("TAG", "addNewSession: ${it.message}")
        }
    }

    private var mSwitchListener = object : SwitchStateListener {
        override fun onChecked(view: View, isChecked: Boolean, data: Any?) {
            if (isChecked) {
//                setSessionBookingStatus(view, isChecked, data)

            } else {
//                setSessionBookingStatus(view, false, data)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSessionsDoctorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(DoctorsSessionsViewModel::class.java)
        mUId = getUId()
        getAllSessions()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        val menuInflater = requireActivity().menuInflater
//        menuInflater.inflate(R.menu.session_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_add_session -> {
//                startActivity(Intent(requireActivity(), AddSessionActivity::class.java))
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun setSessionBookingStatus(view: View, checked: Boolean, data: Any?) {
        val session = data as Session
        val sessionMap = mapOf<String, Any>("booking" to checked)
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(mUId.toString())
            .collection(
                Constants.COLLECTION_SESSIONS
            ).document(session.sessionId.toString()).update(sessionMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (checked)
                        AppUtils.showToast(requireContext(), "Booking opened")
                    else
                        AppUtils.showToast(requireContext(), "Booking closed")
                }
            }.addOnFailureListener {
                AppUtils.showToast(requireContext(), it.message.toString())
            }

    }


    private fun getAllSessions() {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        val query =
            fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(mUId.toString())
                .collection(
                    Constants.COLLECTION_SESSIONS
                )
        query.addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (value != null && error == null) {
                sessionList.clear()
                for (v in value) {
                    val session = v.toObject(Session::class.java)
                    session.sessionId = v.id
                    sessionList.add(session)
                }
            } else {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(requireActivity(), error?.message.toString())
            }
            if (sessionList.isNotEmpty()) {
                setUpRecycler(sessionList)
                mBinding.noDataTv.visibility = View.GONE
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            } else {
                sessionList.clear()
                mBinding.noDataTv.visibility = View.VISIBLE
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
            }
            if (::mAdapter.isInitialized)
                mAdapter.notifyDataSetChanged()

        }

    }

    private fun deleteSession(view: View, documentId: String?) {
        AppUtils.showHideProgressBar(mBinding.progressBar, View.VISIBLE)
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(mUId.toString())
            .collection(
                Constants.COLLECTION_SESSIONS
            ).document(documentId.toString()).delete().addOnSuccessListener {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(requireActivity(), getString(R.string.session_deleted))
            }.addOnFailureListener {
                AppUtils.showHideProgressBar(mBinding.progressBar, View.GONE)
                AppUtils.showToast(requireActivity(), it.message.toString())
            }

    }

    private fun deletePopUp(view: View, documentId: String?) {
        val popUp = PopupMenu(requireActivity(), view)
        popUp.menuInflater.inflate(R.menu.menu_session_options, popUp.menu)
        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_delete -> {
                    deleteSession(view, documentId)
                }
            }
            true
        }
        popUp.show()
    }

    private fun setUpRecycler(sessionList: java.util.ArrayList<Session>) {
        mAdapter =
            SessionsAdapterPatient(sessionList, mRecyclerListener, mSwitchListener)
        mBinding.sessionRecycler.layoutManager =
            LinearLayoutManager(activity)
        mBinding.sessionRecycler.setHasFixedSize(true)
        mBinding.sessionRecycler.adapter = mAdapter

    }

    private fun getUId(): String? {
        arguments.let {
            mViewModel.mDoctor = it?.getSerializable(CommonKeys.KEY_DATA) as Doctor
        }
        val uid = mViewModel.mDoctor?.uId
        Log.d("TAG", "getUId: ${mViewModel.mDoctor}")
        return uid
    }
}
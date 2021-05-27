package com.cybereast.modernqueue.doctor.ui.fragments.sessions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.adapters.SessionsAdapter
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentSessionsBinding
import com.cybereast.modernqueue.doctor.ui.activities.AddSessionActivity
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.listeners.SwitchStateListener
import com.cybereast.modernqueue.models.Session
import com.cybereast.modernqueue.utils.AppUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class SessionsFragment : Fragment() {

    private lateinit var sessionsViewModel: SessionsViewModel
    private lateinit var mBinding: FragmentSessionsBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private lateinit var doctorDocumentRef: DocumentReference
    private val mAuth = FirebaseAuth.getInstance()
    var mUId = mAuth.currentUser?.uid
    private var sessionList: ArrayList<Session> = ArrayList()
    private lateinit var mAdapter: SessionsAdapter
    private var mRecyclerListener = object : RecyclerItemClickListener {
        override fun onClick(view: View, data: Any?) {
            val session = data as Session
            findNavController().navigate(R.id.action_navigation_session_to_navigation_bookings)
            Log.d("TAG", "onClick: ${session.documentId}")
        }

        override fun onItemChildClick(view: View, data: Any?) {
            val session = data as Session
            deletePopUp(view, session.documentId)

        }

    }
    private var mSwitchListener = object : SwitchStateListener {
        override fun onChecked(view: View, isChecked: Boolean, data: Any?) {
            if (isChecked) {
                setSessionBookingStatus(view, isChecked, data)

            } else {
                setSessionBookingStatus(view, false, data)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSessionsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getAllSessions()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuInflater = requireActivity().menuInflater
        menuInflater.inflate(R.menu.session_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_session -> {
                startActivity(Intent(requireActivity(), AddSessionActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSessionBookingStatus(view: View, checked: Boolean, data: Any?) {
        val session = data as Session
        val sessionMap = mapOf<String, Any>("booking" to checked)
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(mUId.toString())
            .collection(
                Constants.COLLECTION_SESSIONS
            ).document(session.documentId.toString()).update(sessionMap).addOnCompleteListener {
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
                    session.documentId = v.id
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
            SessionsAdapter(sessionList, mRecyclerListener, mSwitchListener)
        mBinding.sessionRecycler.layoutManager =
            LinearLayoutManager(activity)
        mBinding.sessionRecycler.setHasFixedSize(true)
        mBinding.sessionRecycler.adapter = mAdapter

    }

}
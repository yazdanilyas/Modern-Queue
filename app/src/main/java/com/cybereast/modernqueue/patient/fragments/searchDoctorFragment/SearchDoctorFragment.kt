package com.cybereast.modernqueue.patient.fragments.searchDoctorFragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybereast.modernqueue.adapters.DoctorsAdapter
import com.cybereast.modernqueue.base.BaseInterface
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.databinding.FragmentSearchDoctorBinding
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Doctor
import com.cybereast.modernqueue.patient.fragments.doctorProfileFragment.DoctorProfileFragment
import com.cybereast.modernqueue.utils.ActivityUtils
import com.cybereast.modernqueue.utils.AppUtils
import com.cybereast.modernqueue.utils.CommonKeys
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList


class SearchDoctorFragment : Fragment(), BaseInterface {
    companion object {
        fun newInstance() = SearchDoctorFragment()
        fun newInstance(mBundle: Bundle?) = SearchDoctorFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentSearchDoctorBinding
    private var fireStoreDbRef = FirebaseFirestore.getInstance()
    private var doctorList: ArrayList<Doctor> = ArrayList()
    private var searchedList: ArrayList<Doctor> = ArrayList()
    private lateinit var mAdapter: DoctorsAdapter
    private var mRecyclerListener = object : RecyclerItemClickListener {
        override fun onClick(data: Any?, position: Int) {

        }

        override fun onItemChildClick(view: View, data: Any?) {

        }

        override fun onSeeProfile(data: Any?, position: Int) {
            val doctor = data as ArrayList<Doctor>
            val uId = doctor[position].uId
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_UID, uId)
            ActivityUtils.launchFragment(
                requireContext(),
                DoctorProfileFragment::class.java.name,
                bundle
            )
            Log.d("TAG", "onSeeProfile: $uId")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSearchDoctorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        getAllDoctors()
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun setListeners() {
        mBinding.searchButton.setOnClickListener {
            showProgressBar()
            val query = mBinding.searchEt.text.toString()
            if (!TextUtils.isEmpty(query)) {
                if (doctorList.isNotEmpty())
                    doctorList.clear()
                searchDoctor(query.capitalize(Locale.ENGLISH))

            } else {
                getAllDoctors()
            }
        }
    }

    private fun searchDoctor(searchQuery: String) {
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    searchList(document, searchQuery)
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                AppUtils.showToast(requireActivity(), exception.message.toString())
            }

    }

    private fun searchList(document: QueryDocumentSnapshot?, searchQuery: String) {
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(document?.id.toString())
            .collection(Constants.COLLECTION_PROFILE).orderBy("firstName").startAt(searchQuery)
            .endAt(searchQuery + '\uf8ff')
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->

                if (value != null && error == null) {
                    for (v in value) {
                        val doctor = v.toObject(Doctor::class.java)
                        doctorList.add(doctor)
                        Log.d("TAG", "searchedDoctorData: ${v.toObject(Doctor::class.java)}")
                    }
                } else {
                    hideProgressBar()
                    AppUtils.showToast(requireActivity(), error?.message.toString())
                }
                if (doctorList.isNotEmpty()) {
                    setUpRecycler(doctorList)
                    mBinding.noDataTv.visibility = View.GONE
                    hideProgressBar()
                } else {
                    doctorList.clear()
                    mBinding.noDataTv.visibility = View.VISIBLE
                    hideProgressBar()
                }
                if (::mAdapter.isInitialized)
                    mAdapter.notifyDataSetChanged()
            }
    }

    private fun getAllDoctors() {
        showProgressBar()
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    getDoctorData(document)
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }.addOnFailureListener { exception ->
                hideProgressBar()
                AppUtils.showToast(requireActivity(), exception.message.toString())
            }


    }

    private fun getDoctorData(doc: QueryDocumentSnapshot?) {
        fireStoreDbRef.collection(Constants.COLLECTION_DOCTORS).document(doc?.id.toString())
            .collection(Constants.COLLECTION_PROFILE)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (value != null && error == null) {
                    for (v in value) {
                        val doctor = v.toObject(Doctor::class.java)
                        doctorList.add(doctor)
                        Log.d("TAG", "getDoctorData: ${v.toObject(Doctor::class.java)}")
                    }
                } else {
                    hideProgressBar()
                    AppUtils.showToast(requireActivity(), error?.message.toString())
                }
                if (doctorList.isNotEmpty()) {
                    setUpRecycler(doctorList)
                    mBinding.noDataTv.visibility = View.GONE
                    hideProgressBar()
                } else {
                    doctorList.clear()
                    mBinding.noDataTv.visibility = View.VISIBLE
                    hideProgressBar()
                }
                if (::mAdapter.isInitialized)
                    mAdapter.notifyDataSetChanged()
            }


    }

    private fun setUpRecycler(doctorList: java.util.ArrayList<Doctor>) {
        mAdapter = DoctorsAdapter(doctorList, mRecyclerListener)
        mBinding.doctorsRecycler.layoutManager = LinearLayoutManager(activity)
        mBinding.doctorsRecycler.adapter = mAdapter
    }


}
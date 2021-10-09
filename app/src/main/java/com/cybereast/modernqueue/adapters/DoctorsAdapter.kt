package com.cybereast.modernqueue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.ItemDoctorRecyclerBinding
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Doctor
import java.util.*

class DoctorsAdapter(
    private val doctorList: ArrayList<Doctor>,
    private val mRecyclerListener: RecyclerItemClickListener,
) :
    RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder>() {
    private lateinit var mBinding: ItemDoctorRecyclerBinding

    class DoctorsViewHolder(val binding: ItemDoctorRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_doctor_recycler, parent, false)
        return DoctorsViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: DoctorsViewHolder, position: Int) {
        holder.binding.obj = doctorList[position]
        holder.binding.itemWrapper.setOnClickListener {
            mRecyclerListener.onClick(doctorList[position], position)
        }
        holder.binding.seeProfileButton.setOnClickListener {
            mRecyclerListener.onSeeProfile(doctorList, position)
        }

    }

    override fun getItemCount(): Int {
        return doctorList.size
    }
}
package com.cybereast.modernqueue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.ItemBookingPBinding
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Booking
import java.util.*

class BookingAdapterP(
    private val bookingList: ArrayList<Booking>,
    private val mRecyclerListener: RecyclerItemClickListener,
) :
    RecyclerView.Adapter<BookingAdapterP.SessionViewHolder>() {
    private lateinit var mBinding: ItemBookingPBinding

    class SessionViewHolder(val binding: ItemBookingPBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_booking_p, parent, false)
        return SessionViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.binding.obj = bookingList[position]
        holder.binding.optionImg.setOnClickListener {
            mRecyclerListener.onItemChildClick(it, bookingList[position])
        }
        holder.binding.containerCv.setOnClickListener {

            mRecyclerListener.onClick(bookingList[position], position)
        }

    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}
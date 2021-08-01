package com.cybereast.modernqueue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.ItemBookingBinding
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.models.Booking
import java.util.*

class BookingAdapter(
    private val bookingList: ArrayList<Booking>,
    private val mRecyclerListener: RecyclerItemClickListener?,
) :
        RecyclerView.Adapter<BookingAdapter.SessionViewHolder>() {
    private lateinit var mBinding: ItemBookingBinding

    class SessionViewHolder(val binding: ItemBookingBinding) :
            RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_booking, parent, false)
        return SessionViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.binding.obj = bookingList[position]
        holder.binding.optionImg.setOnClickListener {
            mRecyclerListener?.onItemChildClick(it, bookingList[position])
        }

    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}
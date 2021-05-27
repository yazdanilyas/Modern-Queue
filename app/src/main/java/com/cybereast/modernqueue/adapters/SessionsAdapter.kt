package com.cybereast.modernqueue.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.databinding.ItemSessionRecyclerBinding
import com.cybereast.modernqueue.listeners.RecyclerItemClickListener
import com.cybereast.modernqueue.listeners.SwitchStateListener
import com.cybereast.modernqueue.models.Session
import java.util.*

class SessionsAdapter(
    private val sessionList: ArrayList<Session>,
    private val mRecyclerListener: RecyclerItemClickListener,
    private val mSwitchListener: SwitchStateListener
) :
    RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {
    private lateinit var mBinding: ItemSessionRecyclerBinding

    class SessionViewHolder(val binding: ItemSessionRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_session_recycler, parent, false)
        return SessionViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.binding.obj = sessionList[position]
        holder.binding.sessionItem.setOnClickListener {
            mRecyclerListener.onClick(it, sessionList[position])
        }
        holder.binding.optionImg.setOnClickListener {
            mRecyclerListener.onItemChildClick(it, sessionList[position])
        }
        holder.binding.bookingSwitch.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    holder.binding.bookingSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
                        mSwitchListener.onChecked(compoundButton, isChecked, sessionList[position])
                    }
                    holder.binding.bookingSwitch.performClick()
                    true
                }

                else -> false
            }
        }

    }

    override fun getItemCount(): Int {
        return sessionList.size
    }
}
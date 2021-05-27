package com.cybereast.modernqueue.listeners

import android.view.View

interface RecyclerItemClickListener {
    fun onClick(view: View, data: Any?)
    fun onItemChildClick(view: View, data: Any?)
}
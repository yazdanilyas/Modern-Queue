package com.cybereast.modernqueue.patient.fragments.bookingDetailFragment

import androidx.lifecycle.ViewModel
import com.cybereast.modernqueue.models.Booking

class BookingkDetailFragmentViewModel : ViewModel() {
    var sessionId: String? = null

    // TODO: Implement the ViewModel
    val bookingList: ArrayList<Booking> = ArrayList()
}
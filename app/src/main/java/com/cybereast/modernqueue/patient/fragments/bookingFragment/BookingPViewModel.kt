package com.cybereast.modernqueue.patient.fragments.bookingFragment

import androidx.lifecycle.ViewModel
import com.cybereast.modernqueue.models.Booking
import java.util.ArrayList

class BookingPViewModel : ViewModel() {
    var bookingList: ArrayList<Booking> = ArrayList()
}
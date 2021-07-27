package com.cybereast.modernqueue.models

class Booking(
        var doctor: Doctor? = null,
        var session: Session? = null,
        var bookingStatus: String? = null,
        var patientId: String? = null,
        var patientName: String? = null,
        var patientPhone: String? = null,
)
package com.cybereast.modernqueue.models

class Booking(
    var bookingId: String? = null,
    var uId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var hospitalName: String? = null,
    var speciality: String? = null,
    var consultancyFee: Int? = null,
    var available: Boolean? = false,

    var sessionId: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val noOfTokens: Int = 0,
    val booking: Boolean? = false,

    var bookingStatus: String? = null,
    var patientId: String? = null,
    var patientName: String? = null,
    var patientPhone: String? = null,
)
package com.cybereast.modernqueue.models

import java.io.Serializable

data class Doctor(
    var uId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var password: String? = null,
    var hospitalName: String? = null,
    var country: String? = null,
    var state: String? = null,
    var district: String? = null,
    var speciality: String? = null,
    var consultancyFee: Int = 0,
    var available: Boolean? = false
):Serializable

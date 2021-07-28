package com.cybereast.modernqueue.models

class Session(
    var sessionId: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val noOfTokens: Int = 0,
    val booking: Boolean? = false
)
package com.cybereast.modernqueue.application

import android.app.Application
import com.google.firebase.FirebaseApp

class ModernQueueApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
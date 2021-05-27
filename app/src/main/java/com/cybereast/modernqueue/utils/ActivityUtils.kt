package com.cybereast.modernqueue.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.doctor.ui.activities.ChooserActivity
import com.cybereast.modernqueue.doctor.ui.activities.DashboardActivity

object ActivityUtils {
    fun startActivity(activity: AppCompatActivity, destClassName: Class<*>) {
        val intent = Intent(activity, destClassName)
        activity.startActivity(intent)
    }

    fun startActivity(activity: FragmentActivity, destClassName: Class<*>) {
        val intent = Intent(activity, destClassName)
        activity.startActivity(intent)
    }

    fun startActivity(
        activity: ChooserActivity,
        destClassName: Class<DashboardActivity>,
        bundle: Bundle
    ) {
        val intent = Intent(activity, destClassName)
        intent.putExtra(Constants.KEY_UID, bundle)
        activity.startActivity(intent)
    }
}
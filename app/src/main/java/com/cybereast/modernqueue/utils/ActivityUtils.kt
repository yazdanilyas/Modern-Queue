package com.cybereast.modernqueue.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.cybereast.modernqueue.constants.Constants
import com.cybereast.modernqueue.doctor.ui.activities.ChooserActivity
import com.cybereast.modernqueue.doctor.ui.activities.DashboardActivity
import com.cybereast.modernqueue.utils.CommonKeys.KEY_FRAGMENT
import com.cybereast.modernqueue.wrapper.FrameActivity

object ActivityUtils {

    fun launchFragment(context: Context, pClassName: String) {
        val profileFragment = Intent(context, FrameActivity::class.java)
        profileFragment.putExtra(KEY_FRAGMENT, pClassName)
        context.startActivity(profileFragment)
    }

    fun launchFragment(context: Context, pClassName: String, pBundle: Bundle) {
        val intent = Intent(context, FrameActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, pClassName)
        intent.putExtra(CommonKeys.KEY_DATA, pBundle)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

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
package com.cybereast.modernqueue.wrapper

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.base.BaseActivity
import com.cybereast.modernqueue.utils.CommonKeys.KEY_DATA
import com.cybereast.modernqueue.utils.CommonKeys.KEY_FRAGMENT


class FrameActivity : BaseActivity() {

    companion object {
        val TAG: String = FrameActivity::class.java.name
    }

    private lateinit var mFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame)
        val intent = intent
        val fragmentName = intent.getStringExtra(KEY_FRAGMENT)
        if (TextUtils.isEmpty(fragmentName)) {
            throw IllegalStateException("Fragment Name is Null")
        }
        var bundle = intent.getBundleExtra(KEY_DATA)
        if (bundle == null) {
            bundle = Bundle.EMPTY
        }
        val fragmentManager = supportFragmentManager
        mFragment = fragmentManager.fragmentFactory.instantiate(
            ClassLoader.getSystemClassLoader(),
            fragmentName!!
        )
        mFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(
            R.id.container,
            mFragment
        )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::mFragment.isInitialized) {
            mFragment.onActivityResult(requestCode, resultCode, data)
        } else {
            Log.e(TAG, "Fragment is not Initialized")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mFragment.isInitialized) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            Log.e(TAG, "Fragment is not Initialized")
        }
    }


}

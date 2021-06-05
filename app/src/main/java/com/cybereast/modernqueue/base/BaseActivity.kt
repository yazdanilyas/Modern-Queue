package com.cybereast.modernqueue.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.cybereast.modernqueue.wrapper.FrameActivity.Companion.TAG

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    protected open fun replaceFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

    protected fun setUpActionBar(toolBar: Toolbar, title: String, showBack: Boolean) {
        try {
            setSupportActionBar(toolBar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(showBack)
                actionBar.title = title
                actionBar.setDisplayShowTitleEnabled(true)
                actionBar.setDisplayShowTitleEnabled(true)
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "setUpActionBar: Failed to setup Action bar", e)
        }
    }

}
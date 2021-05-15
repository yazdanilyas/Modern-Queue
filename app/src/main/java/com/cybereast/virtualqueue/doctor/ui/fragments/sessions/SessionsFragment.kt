package com.cybereast.virtualqueue.doctor.ui.fragments.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cybereast.virtualqueue.R

class SessionsFragment : Fragment() {

    private lateinit var sessionsViewModel: SessionsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sessionsViewModel =
                ViewModelProvider(this).get(SessionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sessions, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        sessionsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
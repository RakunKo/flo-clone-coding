package com.example.flo.ui.main.home.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomePannel1Binding
import com.example.flo.databinding.FragmentHomePannel2Binding

class HomePannelFragment2 : Fragment() {
    lateinit var binding : FragmentHomePannel2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePannel2Binding.inflate(inflater,container, false)
        return binding.root
    }
}
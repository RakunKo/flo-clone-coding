package com.example.flo.ui.main.home.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomePannel1Binding

class HomePannelFragment1 : Fragment() {
    lateinit var binding : FragmentHomePannel1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePannel1Binding.inflate(inflater,container, false)
        return binding.root
    }
}
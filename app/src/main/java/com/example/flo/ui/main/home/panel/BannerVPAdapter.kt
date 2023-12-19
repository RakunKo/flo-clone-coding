package com.example.flo.ui.main.home.panel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.home.panel.HomePannelAdverFragment1
import com.example.flo.ui.main.home.panel.HomePannelAdverFragment2

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment){
    private  val fragmentList : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> HomePannelAdverFragment1()
            else-> HomePannelAdverFragment2()
        }
    }
}
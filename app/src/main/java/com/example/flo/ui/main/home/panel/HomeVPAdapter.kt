package com.example.flo.ui.main.home.panel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.home.panel.HomePannelFragment1
import com.example.flo.ui.main.home.panel.HomePannelFragment2
import com.example.flo.ui.main.home.panel.HomePannelFragment3

class HomeVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment){
    private  val fragmentList : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int =3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> HomePannelFragment1()
            1-> HomePannelFragment2()
            else-> HomePannelFragment3()
        }
    }
}
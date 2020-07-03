package com.possystem.posapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BottomNavigationViewPagerAdapter(fm: FragmentActivity, private var arrayFragments: ArrayList<Fragment>) : FragmentStateAdapter(fm){


    override fun getItemCount() = arrayFragments.size

    override fun createFragment(position: Int): Fragment {
        return arrayFragments[position]
    }
}
package com.example.stacklounge

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.stacklounge.Fragment.FragmentMainCommunity
import com.example.stacklounge.Fragment.FragmentMainFavorite
import com.example.stacklounge.Fragment.FragmentMainSearch
import com.example.stacklounge.Fragment.FragmentMainUser

class AdapterMainFragment(fm : FragmentManager, private val fragmentCount : Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) : Fragment {
        return when(position){
            0 -> FragmentMainFavorite()
            1 -> FragmentMainSearch()
            2 -> FragmentMainCommunity()

            else  -> FragmentMainUser()
        }
    }

    override fun getCount(): Int = fragmentCount
}
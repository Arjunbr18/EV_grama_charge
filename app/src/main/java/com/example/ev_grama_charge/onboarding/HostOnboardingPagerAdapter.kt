package com.example.ev_grama_charge.onboarding

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ev_grama_charge.onboarding.fragments.HostBasicInfoFragment
import com.example.ev_grama_charge.onboarding.fragments.HostLocationFragment

class HostOnboardingPagerAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when(position) {

            0 -> HostBasicInfoFragment()

            else -> HostLocationFragment()
        }
    }
}
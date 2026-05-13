package com.example.ev_grama_charge.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ev_grama_charge.databinding.ActivityHostOnboardingBinding

class HostOnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHostOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {

        val adapter = HostOnboardingPagerAdapter(this)

        binding.viewPager.adapter = adapter

        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    binding.progressIndicator.progress = position + 1
                }
            }
        )
    }
}
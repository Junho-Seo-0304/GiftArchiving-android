package com.nexters.giftarchiving.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.base.BaseFragment
import com.nexters.giftarchiving.databinding.FragmentHomeBinding
import com.nexters.giftarchiving.extension.observe
import com.nexters.giftarchiving.ui.viewpager.adapter.HomeViewPagerAdapter
import com.nexters.giftarchiving.viewmodel.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel

internal class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val layoutId = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.homeTabLayout
        val viewPager = binding.homeViewpager
        val tabTextList = arrayListOf(getString(R.string.home_taken_button_name),getString(R.string.home_given_button_name))
        viewPager.isUserInputEnabled = false
        viewPager.adapter = HomeViewPagerAdapter(this)
        viewPager.setPageTransformer(ViewPager2.PageTransformer { page, position ->
            page.apply {
                val pageWidth = width
                when {
                    position < -0.5f -> {
                        alpha = 0f
                        translationX = pageWidth * -position
                        translationZ = -1f
                    }
                    position <= 0.5f -> {
                        translationX = pageWidth*-position
                        alpha = 1f
                        translationZ = 0f
                    }
                    else -> {
                        translationX = pageWidth*position
                        alpha = 0f
                    }
                }
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = String.format(tabTextList[position])
        }.attach()

        observe(viewModel.currentBgColor){
            binding.logo.backgroundTintList = ContextCompat.getColorStateList(requireContext(),it)
            if (it==R.color.yellow){
                binding.logo.setImageResource(R.drawable.ic_logo_bgcolor_none_black)
            } else{
                binding.logo.setImageResource(R.drawable.ic_logo_bgcolor_none_white)
            }
        }

        observe(viewModel.currentFrame){
            binding.logo.background = when(it){
                "SQUARE" -> ContextCompat.getDrawable(requireContext(),R.drawable.frame_background_rectangle)
                "CIRCLE" -> ContextCompat.getDrawable(requireContext(),R.drawable.frame_background_oval)
                else -> ContextCompat.getDrawable(requireContext(),R.drawable.frame_background_window)
            }
        }
    }
}
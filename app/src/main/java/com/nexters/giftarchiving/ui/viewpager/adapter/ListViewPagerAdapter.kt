package com.nexters.giftarchiving.ui.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nexters.giftarchiving.model.GiftListResponse
import com.nexters.giftarchiving.model.GiftResponse
import com.nexters.giftarchiving.ui.ListType1Fragment
import com.nexters.giftarchiving.ui.ListType2Fragment
import com.nexters.giftarchiving.viewmodel.ListViewModel

internal class ListViewPagerAdapter(
    fragment: Fragment,
    val giftList: List<GiftResponse>,
    val viewModel: ListViewModel
) : FragmentStateAdapter(fragment) {

    companion object {
        private const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) {
            ListType1Fragment(giftList, viewModel)
        }else {
            ListType2Fragment(giftList, viewModel)
        }
    }
}
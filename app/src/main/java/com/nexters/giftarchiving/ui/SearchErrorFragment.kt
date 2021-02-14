package com.nexters.giftarchiving.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.base.BaseFragment
import com.nexters.giftarchiving.databinding.FragmentSearchErrorBinding
import com.nexters.giftarchiving.viewmodel.SearchViewModel

internal class SearchErrorFragment : BaseFragment<SearchViewModel, FragmentSearchErrorBinding>() {
    override val layoutId = R.layout.fragment_search_error
    override val viewModel: SearchViewModel by viewModels({requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
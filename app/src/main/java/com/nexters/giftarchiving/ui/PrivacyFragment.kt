package com.nexters.giftarchiving.ui

import android.os.Bundle
import android.view.View
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.base.BaseFragment
import com.nexters.giftarchiving.databinding.FragmentPrivacyBinding
import com.nexters.giftarchiving.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

internal class PrivacyFragment : BaseFragment<SettingsViewModel, FragmentPrivacyBinding>() {
    override val layoutId = R.layout.fragment_privacy
    override val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
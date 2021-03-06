package com.nexters.giftarchiving.di

import com.nexters.giftarchiving.viewmodel.AboutUsViewModel
import com.nexters.giftarchiving.viewmodel.ConfirmBottomSheetViewModel
import com.nexters.giftarchiving.viewmodel.CropViewModel
import com.nexters.giftarchiving.viewmodel.DetailViewModel
import com.nexters.giftarchiving.viewmodel.HomeViewModel
import com.nexters.giftarchiving.viewmodel.ListViewModel
import com.nexters.giftarchiving.viewmodel.LoginViewModel
import com.nexters.giftarchiving.viewmodel.MainViewModel
import com.nexters.giftarchiving.viewmodel.SearchViewModel
import com.nexters.giftarchiving.viewmodel.SettingsViewModel
import com.nexters.giftarchiving.viewmodel.ShareInstagramViewModel
import com.nexters.giftarchiving.viewmodel.ShareViewModel
import com.nexters.giftarchiving.viewmodel.SplashViewModel
import com.nexters.giftarchiving.viewmodel.WriteViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MainViewModel() }
    factory { WriteViewModel(get(), get()) }
    factory { CropViewModel() }
    factory { ShareViewModel() }
    factory { ShareInstagramViewModel() }
    factory { SplashViewModel() }
    factory { LoginViewModel(get(), get()) }
    factory { HomeViewModel(get(), get()) }
    factory { ListViewModel(get(), get()) }
    factory { SettingsViewModel(get(), get()) }
    factory { SearchViewModel(get(), get()) }
    factory { DetailViewModel(get()) }
    factory { ConfirmBottomSheetViewModel() }
    factory { AboutUsViewModel() }
}
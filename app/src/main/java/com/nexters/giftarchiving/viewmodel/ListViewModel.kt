package com.nexters.giftarchiving.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nexters.giftarchiving.base.BaseViewModel
import com.nexters.giftarchiving.model.GiftListResponse
import com.nexters.giftarchiving.repository.GiftRepository
import com.nexters.giftarchiving.repository.PreferenceRepository
import com.nexters.giftarchiving.ui.ListFragmentArgs
import com.nexters.giftarchiving.ui.ListFragmentDirections
import com.nexters.giftarchiving.util.BackDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class ListViewModel(
    private val giftRepository: GiftRepository,
    private val preferenceRepository: PreferenceRepository
) : BaseViewModel() {
    val userId = preferenceRepository.getUserId()
    val giftList = MutableLiveData(GiftListResponse(listOf(),0,0,0))
    val listType = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    var isReceived = true
    var type = true

    init {
        viewModelScope.launch {
            navArgs<ListFragmentArgs>()
                .collect {
                    title.value = it.title
                    isReceived = it.title=="받은 선물"
                    giftList.value = it.response
                }
            listType.value=type
            Log.e("ListVM",giftList.value.toString())
        }
    }

    val onClickListSwitchListener = View.OnClickListener(){
        onClickListSwitchButton()
    }

    private fun onClickListSwitchButton(){
        type = !type
        listType.value = type
    }

    fun onClickBack() {
        navDirections.value = BackDirections()
    }

    fun onClickSearch(){
        navDirections.value = ListFragmentDirections.actionListFragmentToSearchFragment()
    }
}
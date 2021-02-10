package com.nexters.giftarchiving.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nexters.giftarchiving.base.BaseViewModel
import com.nexters.giftarchiving.data.write.WriteCategoryMenu
import com.nexters.giftarchiving.data.write.WriteEmotionMenu
import com.nexters.giftarchiving.data.write.WriteInformationMenu
import com.nexters.giftarchiving.data.write.WritePurposeMenu
import com.nexters.giftarchiving.ui.WriteFragmentArgs
import com.nexters.giftarchiving.ui.WriteFragmentDirections
import com.nexters.giftarchiving.util.BackDirections
import com.nexters.giftarchiving.util.LiveEvent
import com.nexters.giftarchiving.ui.data.BackgroundColorTheme
import com.nexters.giftarchiving.ui.data.write.WriteMenu
import com.nexters.giftarchiving.ui.data.write.WriteStickerTabLayoutTheme
import com.xiaopo.flying.sticker.Sticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class WriteViewModel : BaseViewModel() {
    val editedImage = MutableLiveData<Bitmap?>()
    val backgroundColorTheme = MutableLiveData(BackgroundColorTheme.MONO)
    val stickerViewPagerTheme = MutableLiveData(WriteStickerTabLayoutTheme.DARK_MODE)
    val date = MutableLiveData(LocalDate.now())
    val category = MutableLiveData(WriteCategoryMenu())
    val purpose = MutableLiveData(WritePurposeMenu())
    val emotion = MutableLiveData(WriteEmotionMenu())
    val name = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val showMenuType = LiveEvent<WriteMenu>()
    val hideMenuType = LiveEvent<WriteMenu>()
    val changeDate = LiveEvent<Unit?>()
    val addSticker = LiveEvent<Unit?>()
    val loadGallery = LiveEvent<Unit?>()
    val isSaved = LiveEvent<Unit?>()

    var stickerList = mutableListOf<Sticker>()
    var baseImageUri: Uri? = null
    private var isReceiveGift = true

    init {
        viewModelScope.launch {
            navArgs<WriteFragmentArgs>()
                .collect {
                    editedImage.value = it.bitmap
                    isReceiveGift = it.isReceiveGift
                }
        }
    }

    fun setInformationMenu(item: WriteInformationMenu) {
        when (showMenuType.value) {
            WriteMenu.INFORMATION_CATEGORY -> category.value = item as WriteCategoryMenu
            WriteMenu.INFORMATION_PURPOSE -> purpose.value = item as WritePurposeMenu
            WriteMenu.INFORMATION_EMOTION -> emotion.value = item as WriteEmotionMenu
            else -> return
        }

        hideMenuType.value = showMenuType.value
    }

    fun setNewImage(uri: Uri, img: Bitmap) {
        baseImageUri = uri
        editedImage.value = img
    }

    fun loadGallery() {
        loadGallery.call()
    }

    fun loadCropEditor() {
        if (baseImageUri != null) {
            navDirections.value =
                WriteFragmentDirections.actionWriteFragmentToCropFragment(baseImageUri.toString())
        } else {
            toast.value = NOTICE_SELECT_IMAGE
        }
    }

    fun setBackgroundColor(colorTheme: BackgroundColorTheme) {
        backgroundColorTheme.value = colorTheme
        stickerViewPagerTheme.value = when (colorTheme.isDarkMode) {
            true -> WriteStickerTabLayoutTheme.DARK_MODE
            false -> WriteStickerTabLayoutTheme.LIGHT_MODE
        }
    }

    fun attachSticker() {
        addSticker.call()
        hideMenuType.value = WriteMenu.STICKER
    }

    fun onClickBack() {
        navDirections.value = BackDirections()
    }

    fun setShowMenuType(menuType: WriteMenu) {
        showMenuType.value = menuType
    }

    fun setHideMenuType(menuType: WriteMenu) {
        hideMenuType.value = menuType
    }

    fun changeDate() {
        changeDate.call()
        setHideMenuType(WriteMenu.DATE)
    }

    fun onClickNext() {
        isSaved.call()
    }

    private fun convertLayoutToBitmap(v: View): Bitmap {
        val bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        return bitmap
    }

    companion object {
        @JvmStatic
        val NOTICE_SELECT_IMAGE = "이미지를 선택하세요"

        @JvmStatic
        val NOTICE_FAIL_CONVERT_IMG = "이미지 변환에 실패하였습니다"
    }
}
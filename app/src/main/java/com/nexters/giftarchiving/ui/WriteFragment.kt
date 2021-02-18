package com.nexters.giftarchiving.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.base.BaseFragment
import com.nexters.giftarchiving.databinding.FragmentWriteBinding
import com.nexters.giftarchiving.extension.observe
import com.nexters.giftarchiving.extension.toast
import com.nexters.giftarchiving.ui.data.write.WriteMenu
import com.nexters.giftarchiving.ui.data.write.WriteSticker
import com.nexters.giftarchiving.ui.viewpager.adapter.MenuSlidePagerAdapter
import com.nexters.giftarchiving.ui.viewpager.adapter.StickerSlidePagerAdapter
import com.nexters.giftarchiving.util.BackDirections
import com.nexters.giftarchiving.viewmodel.WriteViewModel
import com.xiaopo.flying.sticker.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDate


internal class WriteFragment : BaseFragment<WriteViewModel, FragmentWriteBinding>() {
    override val layoutId = R.layout.fragment_write
    override val viewModel: WriteViewModel by viewModel()
    override val navArgs by navArgs<WriteFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Bitmap>("image")
            ?.observe(viewLifecycleOwner, Observer { viewModel.setNewImage(it) })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setStickerView()
        setStickerMenuViewPager()
        setContentEditTextSize()
        setBackPressedDispatcher()

        observe(viewModel.showMenuType) { showSelectedMenu(it) }
        observe(viewModel.hideMenuType) { hideSelectedMenu(it) }
        observe(viewModel.changeDate) { changeDate() }
        observe(viewModel.loadGallery) { checkPermissionAndAccessGallery() }
        observe(viewModel.isSaved) { saveGift() }
        observe(viewModel.addSticker) { addSticker() }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        with(viewModel.stickerList) {
            clear()
            addAll(binding.stickerView.stickers)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, it)
                val bitmap = ImageDecoder.decodeBitmap(source)
                viewModel.navDirections.value =
                    WriteFragmentDirections.actionWriteFragmentToCropFragment(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    accessGallery()
                } else {
                    toast("갤러리에 접근할 수 없습니다")
                }
                return
            }
        }
    }

    private fun checkPermission(requestCode: Int, doAccess: () -> Unit) {
        val permission = when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
            else -> null
        }

        permission?.let {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(permission), requestCode)
            } else {
                doAccess()
            }
        }
    }

    private fun setStickerView() {
        with(binding.stickerView) {
            stickers = viewModel.stickerList
            isConstrained = true

            val deleteIcon = BitmapStickerIcon(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_cancel_sticker),
                BitmapStickerIcon.RIGHT_TOP
            ).apply { iconEvent = DeleteIconEvent() }

            val zoomIcon = BitmapStickerIcon(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_scale),
                BitmapStickerIcon.RIGHT_BOTOM
            ).apply { iconEvent = ZoomIconEvent() }

            binding.stickerView.icons = listOf(deleteIcon, zoomIcon)
        }
    }

    private fun setContentEditTextSize() {
        with(binding.contentEt) {
            viewTreeObserver.addOnGlobalLayoutListener {
                height = height
            }
        }
    }

    private fun setBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            with(viewModel) {
                if (currentMenuType.value != null) {
                    hideCurrentMenu()
                } else {
                    navDirections.value = BackDirections()
                }
            }
        }
    }

    private fun showSelectedMenu(menuType: WriteMenu) {
        when (menuType) {
            WriteMenu.INFORMATION_CATEGORY, WriteMenu.INFORMATION_PURPOSE, WriteMenu.INFORMATION_EMOTION -> {
                setInformationMenuViewPager(menuType)
                binding.menuInformationLayout
            }
            WriteMenu.FRAME -> binding.menuFrameLayout
            WriteMenu.STICKER -> {
                if (viewModel.editedImage.value != null) {
                    binding.menuStickerLayout
                } else {
                    viewModel.hideCurrentMenu()
                    toast(WriteViewModel.NOTICE_SELECT_IMAGE)
                    null
                }
            }
            WriteMenu.BACKGROUND_COLOR -> binding.menuBackgroundColorLayout
            WriteMenu.DATE -> {
                loadDate()
                binding.menuDateLayout
            }
        }?.visibility = View.VISIBLE
    }

    private fun hideSelectedMenu(menuType: WriteMenu) {
        when (menuType) {
            WriteMenu.INFORMATION_CATEGORY, WriteMenu.INFORMATION_PURPOSE, WriteMenu.INFORMATION_EMOTION -> binding.menuInformationLayout
            WriteMenu.FRAME -> binding.menuFrameLayout
            WriteMenu.STICKER -> binding.menuStickerLayout
            WriteMenu.BACKGROUND_COLOR -> binding.menuBackgroundColorLayout
            WriteMenu.DATE -> binding.menuDateLayout
        }.visibility = View.GONE
    }

    private fun loadDate() {
        viewModel.date.value?.run {
            val y = year
            val m = monthValue - 1
            val d = dayOfMonth
            binding.datePicker.updateDate(y, m, d)
        }
    }

    private fun changeDate() {
        with(binding.datePicker) {
            val y = year
            val m = month + 1
            val d = dayOfMonth
            viewModel.date.value = LocalDate.of(y, m, d)
        }
    }

    private fun setInformationMenuViewPager(menuType: WriteMenu) {
        with(binding.informationMenuViewpager) {
            adapter = MenuSlidePagerAdapter(
                requireActivity(),
                viewModel,
                menuType,
                viewModel.isReceiveGift
            )
            TabLayoutMediator(binding.informationMenuTabLayout, this) { tab, pos ->

            }.attach()
        }
    }

    private fun setStickerMenuViewPager() {
        with(binding.menuStickerViewpager) {
            adapter = StickerSlidePagerAdapter(requireActivity(), viewModel)
            val stickerType = WriteSticker.values()
            TabLayoutMediator(binding.menuStickerTabLayout, this) { tab, pos ->
                tab.text = WriteSticker.valueOf(stickerType[pos].name).menuTitle
            }.attach()
        }
    }

    private fun checkPermissionAndAccessGallery() {
        checkPermission(REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            accessGallery()
        }
    }

    private fun accessGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun addSticker() {
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, null)
        binding.stickerView.addSticker(DrawableSticker(drawable), Sticker.Position.CENTER)
    }

    private fun saveGift() {
        binding.stickerView.removeStickerHandler()
        val noBgBitmap = binding.stickerView.createBitmap()
        binding.shareIv.setImageBitmap(noBgBitmap)
        viewModel.delayAndCallback {
            val bgBitmap = viewModel.convertLayoutToBitmap(binding.shareLayout)
            viewModel.goNext(requireContext().cacheDir, noBgBitmap, bgBitmap)
        }
    }

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }
}
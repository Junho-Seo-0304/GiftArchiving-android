package com.nexters.giftarchiving.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.base.BaseFragment
import com.nexters.giftarchiving.databinding.FragmentTakenBinding
import com.nexters.giftarchiving.extension.observe
import com.nexters.giftarchiving.ui.viewpager.adapter.ItemViewPagerAdapter
import com.nexters.giftarchiving.viewmodel.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel

internal class TakenFragment : BaseFragment<HomeViewModel, FragmentTakenBinding>() {
    override val layoutId = R.layout.fragment_taken
    override val viewModel: HomeViewModel by viewModel()
    var current = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.homeTakenViewpager
        val bgColors = arrayListOf<Int>(R.color.blue,R.color.orange,R.color.yellow)

        val pageTransformer = PreviewSidePageTransformer()

        viewPager.apply {
            offscreenPageLimit = 1
            clipToPadding = false
            setPageTransformer(pageTransformer)
            observe(viewModel.getAllReceivedGiftListResponse) {
                adapter = ItemViewPagerAdapter(requireContext(),it.giftListGifts,0)
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val valueAnimator = ValueAnimator.ofObject(ArgbEvaluator(),ContextCompat.getColor(context,bgColors[current]),ContextCompat.getColor(context,bgColors[position]))
                    Log.d("position",position.toString())
                    valueAnimator.apply {
                        duration = 500
                        addUpdateListener {
                            viewPager.setBackgroundColor(it.animatedValue as Int)
                        }
                        start()
                    }
                    current=position
                }
            })
            val itemDecoration = HorizontalMarginItemDecoration(
                context,
                R.dimen.viewpager_current_item_margin
            )
            addItemDecoration(itemDecoration)
        }
    }


    inner class PreviewSidePageTransformer : ViewPager2.PageTransformer{

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        override fun transformPage(page: View, position: Float) {
            page.apply {
                translationX = -pageTranslationX * position
            }
        }
    }

    inner class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }
    }
}
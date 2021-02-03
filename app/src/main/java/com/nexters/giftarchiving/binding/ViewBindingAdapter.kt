package com.nexters.giftarchiving.binding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.nexters.giftarchiving.R
import com.nexters.giftarchiving.util.ThemeBackgroundColorChangeAnimator
import com.nexters.giftarchiving.ui.data.BackgroundColorTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun getColorByResource(context: Context, @ColorRes colorId: Int) =
    ContextCompat.getColor(context, colorId)

@BindingAdapter("android:textColor")
fun setFontColor(tv: TextView, colorTheme: BackgroundColorTheme?) {
    val colorRes =
        if (colorTheme?.isDarkMode == false) {
            R.color.colorDarkGray
        } else {
            R.color.colorWhite
        }
    val colorByResource =
        getColorByResource(tv.context, colorRes)
    tv.setTextColor(colorByResource)
}

@BindingAdapter("android:textColorHint")
fun setFontHintColor(et: EditText, colorTheme: BackgroundColorTheme?) {
    val colorRes =
        if (colorTheme?.isDarkMode == false) {
            R.color.colorDarkGray
        } else {
            R.color.colorWhite
        }
    val colorByResource =
        getColorByResource(et.context, colorRes)
    et.setHintTextColor(colorByResource)
}

@BindingAdapter("android:background")
fun setBackgroundColor(v: View, colorTheme: BackgroundColorTheme?) {
    val colorByResource =
        getColorByResource(
            v.context,
            colorTheme?.backgroundColor ?: BackgroundColorTheme.MONO.backgroundColor
        )
    v.setBackgroundColor(colorByResource)
}

@BindingAdapter("popUpBackground")
fun setPopUpBackgroundColor(v: View, colorTheme: BackgroundColorTheme?) {
    val colorByResource =
        getColorByResource(
            v.context,
            colorTheme?.popupBackgroundColor ?: BackgroundColorTheme.MONO.popupBackgroundColor
        )
    v.setBackgroundColor(colorByResource)
}

@BindingAdapter("backgroundWithAnimation")
fun setBackgroundColorWithAnimation(v: View, colorTheme: BackgroundColorTheme?) {
    val themeBackgroundColorRes =
        colorTheme?.backgroundColor ?: BackgroundColorTheme.MONO.backgroundColor
    val themeBackgroundColor = getColorByResource(v.context, themeBackgroundColorRes)

    if (v.background is ColorDrawable) {
        val from = (v.background as ColorDrawable).color
        ThemeBackgroundColorChangeAnimator(v, from, themeBackgroundColor).start()
    } else {
        v.setBackgroundColor(themeBackgroundColor)
    }
}

@BindingAdapter("android:background")
fun setBackgroundWithBitmap(iv: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        iv.setImageBitmap(it)
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(v: View, isVisible: Boolean) {
    v.visibility = when (isVisible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

@BindingAdapter("localDateWithFormat")
fun setLocalDate(tv: TextView, localDate: LocalDate) {
    tv.text = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. EEE", Locale.ENGLISH))
}
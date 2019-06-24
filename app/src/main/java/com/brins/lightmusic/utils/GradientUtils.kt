package com.brins.lightmusic.utils

import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

class GradientUtils {
    companion object{
        @JvmStatic fun create(@ColorInt startColor : Int , @ColorInt endColor : Int,
                              radius : Int, @FloatRange(from = 0.0, to = 1.0) centerX : Float,
                              @FloatRange(from = 0.0, to = 1.0) centerY : Float): GradientDrawable{

            val gradientDrawable = GradientDrawable()
            gradientDrawable.colors = intArrayOf(startColor, endColor)
            gradientDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
            gradientDrawable.gradientRadius = radius.toFloat()
            gradientDrawable.setGradientCenter(centerX, centerY)
            return gradientDrawable
        }
    }
}
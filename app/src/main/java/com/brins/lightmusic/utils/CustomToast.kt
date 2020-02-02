package com.brins.lightmusic.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.brins.lightmusic.R

class CustomToast(context: Context, text: String, duration: Int, type: Int) {

    companion object {
        val TYPE_SUCCESS = 0
        val TYPE_FAILURE = 1

        fun makeText(context: Context, text: String, duration: Int, type: Int): CustomToast {
            return CustomToast(context, text, duration, type)
        }

        fun makeText(
            context: Context, @StringRes text: Int,
            dutation: Int,
            type: Int
        ): CustomToast {
            return CustomToast(context, context.getString(text), dutation, type)
        }
    }

    private val mToast: Toast

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
        val toastTV = view.findViewById<TextView>(R.id.toast_tv)
        toastTV.text = text
        val imageView = view.findViewById<ImageView>(R.id.toast_icon)
        if (type == TYPE_SUCCESS) {
            imageView.setImageResource(R.drawable.toast_icon_sucess)
        } else {
            imageView.setImageResource(R.drawable.toast_icon_none)
        }

        mToast = Toast(context)
        mToast.duration = duration
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.view = view
        mToast.show()
    }
}
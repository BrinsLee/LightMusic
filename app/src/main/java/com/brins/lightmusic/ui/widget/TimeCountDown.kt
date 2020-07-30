package com.brins.lightmusic.ui.widget

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.brins.lightmusic.R

class TimeCountDown(
    millisInFuture: Long,
    countDownInterval: Long,
    var mTextView: TextView,
    var mContext: Context
) : CountDownTimer(millisInFuture, countDownInterval) {
    override fun onFinish() {
        this.mTextView.isEnabled = true
        this.mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.blue))
        this.mTextView.text = mContext.getString(R.string.send_code)
    }

    override fun onTick(millisUntilFinished: Long) {
        this.mTextView.isEnabled = false
        this.mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.btn_presss))
        this.mTextView.text = (millisUntilFinished / 1000).toString() + "s"
    }
}
package com.brins.lightmusic.ui.customview

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.brins.lightmusic.R
import com.brins.lightmusic.player.VideoStateListener


class JZVideoPalyerView @JvmOverloads constructor(
    context: Context,
    attrs: android.util.AttributeSet? = null
) : JzvdStd(context, attrs) {

    override fun init(context: Context?) {
        super.init(context)
    }

    override fun onClick(v: View?) {

        super.onClick(v)
    }

    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }


    override fun onTouch(v: View, event: MotionEvent): Boolean {

        mListener?.onTouch()
        return super.onTouch(v, event)
    }

    override fun startVideo() {
        super.startVideo()
        Log.i(Jzvd.TAG, "startVideo...")
        mListener?.onStart()
    }

    override fun onStateNormal() {
        super.onStateNormal()
        mListener?.onStateNormal()
    }

    override fun onStatePreparing() {
        super.onStatePreparing()
        mListener?.onPreparing()
    }

    override fun onStatePlaying() {
        super.onStatePlaying()
        Log.i(Jzvd.TAG, "onStatePlaying...")
        mListener?.onPlaying()
    }

    override fun onStatePause() {
        super.onStatePause()
        Log.i(Jzvd.TAG, "onStatePause...")

        mListener?.onPause()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //        super.onProgressChanged(seekBar, progress, fromUser);

        mListener?.onProgressChanged(progress)
    }

    override fun onStateError() {
        super.onStateError()
    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        mListener?.onComplete()
    }

    override fun onInfo(what: Int, extra: Int) {
        super.onInfo(what, extra)
        Log.i(Jzvd.TAG, "onInfo...")
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
    }

    override fun startWindowFullscreen() {
        super.startWindowFullscreen()
    }

    override fun startWindowTiny() {
        super.startWindowTiny()
    }

    override fun startDismissControlViewTimer() {
        super.startDismissControlViewTimer()
        Log.i(Jzvd.TAG, "startDismissControlViewTimer...")
        mListener?.onStartDismissControlViewTimer()
    }

    private var mListener: VideoStateListener? = null

    fun getListener(): VideoStateListener? {
        return mListener
    }

    fun setVideoStateListener(listener: VideoStateListener) {
        mListener = listener
    }
}
package com.brins.lightmusic.ui.base

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    private val TAG = "AppBarStateChangeListen"

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var mCurrentState = State.IDLE


    override fun onOffsetChanged(p0: AppBarLayout, p1: Int) {

        onOffsetChanged(p0)
        if (p1 == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(p0, State.EXPANDED)
            }
            mCurrentState = State.EXPANDED
        } else if (abs(p1) >= p0.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(p0, State.COLLAPSED)
            }
            mCurrentState = State.COLLAPSED
        } else if (mCurrentState != State.IDLE) {
            onStateChanged(p0, State.IDLE)
        }
        mCurrentState = State.IDLE
    }

    //状态发生了改变
    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)

    //发生了偏移
    abstract fun onOffsetChanged(appBarLayout: AppBarLayout)

}
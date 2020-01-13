package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity

interface BaseView {

    fun getLifeActivity(): AppCompatActivity

    fun showLoading()

    fun hideLoading()

    fun handleError(error: Throwable)

}
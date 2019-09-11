package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity

interface BaseView<T> {

    fun getLifeActivity(): AppCompatActivity

    fun setPresenter(presenter: T)

    fun showLoading()

    fun hideLoading()

    fun handleError(error: Throwable)

}
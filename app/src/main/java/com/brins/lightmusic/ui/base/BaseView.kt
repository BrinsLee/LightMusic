package com.brins.lightmusic.ui.base


interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun handleError(error: Throwable)

}
package com.brins.lightmusic.utils

import com.hwangjr.rxbus.Bus



class RxBus {


    companion object {
        @JvmStatic
        private val TAG = "RxBus"
        @JvmStatic
        @Volatile
        private var sInstance: Bus? = null

        fun getInstance(): Bus {
            if (sInstance == null) {
                synchronized(RxBus::class.java) {
                    if (sInstance == null) {
                        sInstance = Bus()
                    }
                }
            }
            return sInstance!!
        }


    }
}
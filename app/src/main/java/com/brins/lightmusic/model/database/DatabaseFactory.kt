package com.brins.lightmusic.model.database

import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.model.database.userinfodb.UserInfoDatabaseHelper

object DatabaseFactory {

    private var mUserInfoDB: UserInfoDatabaseHelper? = null

    fun getUserInfoDB(): UserInfoDatabaseHelper {
        if (mUserInfoDB == null) {
            synchronized(UserInfoDatabaseHelper::class.java) {
                if (mUserInfoDB == null) {
                    mUserInfoDB = UserInfoDatabaseHelper(BaseApplication.getInstance().baseContext)
                }
            }
        }
        return mUserInfoDB!!
    }
}
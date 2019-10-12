package com.brins.lightmusic.model.database

import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.database.userinfodb.UserInfoDatabaseHelper
import com.brins.lightmusic.model.userlogin.UserAccountBean
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.model.userlogin.UserProfileBean
import io.reactivex.Single

object DatabaseFactory {

    private var mUserInfoDB: UserInfoDatabaseHelper? = null

    private fun getUserInfoDB(): UserInfoDatabaseHelper {
        if (mUserInfoDB == null) {
            synchronized(UserInfoDatabaseHelper::class.java) {
                if (mUserInfoDB == null) {
                    mUserInfoDB = UserInfoDatabaseHelper(BaseApplication.getInstance().baseContext)
                }
            }
        }
        return mUserInfoDB!!
    }

    fun storeUserInfo(result: UserAccountBean): Single<Long>{
        return getUserInfoDB().insertUserAccount(result)
    }

    fun storeUserProfile(profile : UserProfileBean): Single<Long>{
        return getUserInfoDB().insertUserProfile(profile)
    }

    fun getUserInfo():Single<UserAccountBean>{
        return getUserInfoDB().getUserAccount()
    }

    fun getUserProfile():Single<UserProfileBean>{
        return getUserInfoDB().getUserProfile()
    }

    fun deleteUserInfo(): Single<Int>{
        return getUserInfoDB().deleteUserAccount(AppConfig.userAccount)
    }

    fun deleteUserProfile(): Single<Int>{
        return getUserInfoDB().deleteUserProfile(AppConfig.userProfile)
    }
}
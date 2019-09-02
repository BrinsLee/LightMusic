package com.brins.lightmusic.model.database.userinfodb

import android.content.Context
import androidx.room.Room
import com.brins.lightmusic.model.userlogin.UserAccountBean
import com.brins.lightmusic.model.userlogin.UserProfileBean
import io.reactivex.Maybe
import io.reactivex.Single

class UserInfoDatabaseHelper(context: Context) {
    private val appDatabase = Room.databaseBuilder(context,
        UserInfoDatabase::class.java,"dbUserInfo").build()


    fun insertUserAccount(account: UserAccountBean) : Single<Long>{
        return appDatabase.dao().addUserAccount(account)
    }

    fun insertUserProfile(account: UserProfileBean): Single<Long>{
        return appDatabase.dao().addUserProfile(account)
    }

    fun getUserAccount () :Single<UserAccountBean>{
        return appDatabase.dao().getUserAccount()
    }

    fun getUserProfile () :Single<UserProfileBean>{
        return appDatabase.dao().getUserProfile()
    }

    fun deleteUserAccount(account : UserAccountBean){
        val id = account.mId
        appDatabase.dao().deleteUserAccount(id)
    }
    fun deleteUserProfile(account : UserProfileBean){
        val id = account.mId
        appDatabase.dao().deleteUserProfile(id)
    }
}
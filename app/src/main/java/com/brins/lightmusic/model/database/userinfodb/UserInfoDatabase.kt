package com.brins.lightmusic.model.database.userinfodb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brins.lightmusic.model.userlogin.UserAccountBean
import com.brins.lightmusic.model.userlogin.UserProfileBean

@Database(entities = arrayOf(UserAccountBean::class,UserProfileBean::class),version = 1,exportSchema = false)
abstract class UserInfoDatabase : RoomDatabase() {
    abstract fun dao() : UserInfoDao
}
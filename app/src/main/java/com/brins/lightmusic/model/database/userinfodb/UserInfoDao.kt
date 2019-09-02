package com.brins.lightmusic.model.database.userinfodb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brins.lightmusic.model.userlogin.UserAccountBean
import com.brins.lightmusic.model.userlogin.UserProfileBean
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface UserInfoDao {

    @Insert
    fun addUserAccount(account : UserAccountBean) : Single<Long>
    @Insert
    fun addUserProfile(account : UserProfileBean) : Single<Long>

    @Query("select * from user_account order by ID DESC")
    fun getUserAccount() : Single<UserAccountBean>

    @Query("select * from user_profile order by ID DESC")
    fun getUserProfile(): Single<UserProfileBean>

    @Query("delete from user_account where ID=:id")
    fun deleteUserAccount(id : Long) : Single<Int>

    @Query("delete from user_profile where ID=:id")
    fun deleteUserProfile(id : Long) : Single<Int>
}
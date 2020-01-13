package com.brins.lightmusic.di.component

import android.app.Activity
import com.brins.lightmusic.di.module.ActivityModule
import com.brins.lightmusic.di.scope.ActivityScope
import com.brins.lightmusic.ui.activity.MusicPlayActivity
import com.brins.lightmusic.ui.activity.login.LoginActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun getActivity(): Activity

    fun inject(loginActivity: LoginActivity)

    fun inject(musicPlayActivity: MusicPlayActivity)


}
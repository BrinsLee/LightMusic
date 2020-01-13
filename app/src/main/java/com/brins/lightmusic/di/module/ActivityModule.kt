package com.brins.lightmusic.di.module

import android.app.Activity
import com.brins.lightmusic.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule (var mActivity : Activity){

    @Provides
    @ActivityScope
    fun provideActivity(): Activity {
        return mActivity
    }
}
package com.brins.lightmusic.di.component

import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getContext() : LightMusicApplication
}
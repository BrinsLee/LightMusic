package com.brins.lightmusic.di.module

import com.brins.lightmusic.LightMusicApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (var application : LightMusicApplication){

    @Provides
    @Singleton
    fun provideApplication():LightMusicApplication = application


}

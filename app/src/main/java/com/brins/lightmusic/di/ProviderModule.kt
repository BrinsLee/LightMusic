package com.brins.lightmusic.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * @author lipeilin
 * @date 2020/7/30
 */
@InstallIn(ActivityComponent::class)
@Module
object ProviderModule {

    @Provides
    fun providerFragmentManager(@ActivityContext context: Context) = (context as FragmentActivity).supportFragmentManager
}
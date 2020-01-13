package com.brins.lightmusic.di.module

import android.app.Activity
import androidx.fragment.app.Fragment
import com.brins.lightmusic.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class FragmentModule (var fragment : Fragment){

    @Provides
    @FragmentScope
    fun provideActivity(): Activity {
        return fragment.activity!!
    }
}
package com.codeChallenge.olympicChannel.di.modules

import com.codeChallenge.olympicChannel.view.fragments.FragmentAthleteDetail
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMyFragment(): FragmentAthleteDetail
}
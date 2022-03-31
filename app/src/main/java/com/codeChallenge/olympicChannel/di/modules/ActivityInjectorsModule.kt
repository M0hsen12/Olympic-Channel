package com.codeChallenge.olympicChannel.di.modules

import com.codeChallenge.olympicChannel.view.activities.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityInjectorsModule {


    @ContributesAndroidInjector(modules = [ViewModelModule::class, FragmentBuildersModule::class])
    abstract fun mainActivityInjector(): MainActivity


}
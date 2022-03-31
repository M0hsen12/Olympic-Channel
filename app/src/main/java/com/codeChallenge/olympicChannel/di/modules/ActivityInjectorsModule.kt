package com.codeChallenge.olympicChannel.di.modules

import com.codeChallenge.olympicChannel.di.viewModelsInjections.ViewModelModule
import com.codeChallenge.olympicChannel.view.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityInjectorsModule {


    @ContributesAndroidInjector(modules = [ViewModelModule::class, FragmentBuildersModule::class])

    abstract fun mainActivityInjector(): MainActivity


}
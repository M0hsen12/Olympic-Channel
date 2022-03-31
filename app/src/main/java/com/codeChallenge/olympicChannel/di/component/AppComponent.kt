package com.codeChallenge.olympicChannel.di.component

import android.content.Context
import com.codeChallenge.olympicChannel.App
import com.codeChallenge.olympicChannel.di.modules.ActivityInjectorsModule
import com.codeChallenge.olympicChannel.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityInjectorsModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }

    override fun inject(app: App)



}


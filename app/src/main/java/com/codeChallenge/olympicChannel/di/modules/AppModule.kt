package com.codeChallenge.olympicChannel.di.modules

import com.codeChallenge.olympicChannel.di.data.appManager.AppDataManager
import com.codeChallenge.olympicChannel.di.data.appManager.DataManager
import com.codeChallenge.olympicChannel.di.data.database.DatabaseModule
import com.codeChallenge.olympicChannel.di.data.network.NetworkModule
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
open class AppModule {


    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }


}
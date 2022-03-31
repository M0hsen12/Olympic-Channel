package com.codeChallenge.olympicChannel.di.data.appManager

import android.content.Context
import com.codeChallenge.olympicChannel.di.data.network.NetworkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager
@Inject
constructor(
    override val context: Context,
    override val networkManager: NetworkManager,
//    override val databaseManager: DatabaseManager
    ) : DataManager



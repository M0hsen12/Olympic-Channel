package com.codeChallenge.olympicChannel.di.data.appManager

import android.content.Context
import com.codeChallenge.olympicChannel.di.data.database.DatabaseManager
import com.codeChallenge.olympicChannel.di.data.network.NetworkManager


interface DataManager {

    val context: Context

    val networkManager: NetworkManager

    val databaseManager: DatabaseManager

    // we can add every thing we want in here like dataBase manager , download manager , account manger and ...

}

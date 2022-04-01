package com.codeChallenge.olympicChannel.di.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codeChallenge.olympicChannel.di.data.database.dao.AthleteDao
import com.codeChallenge.olympicChannel.di.data.database.dao.GamesDao
import com.codeChallenge.olympicChannel.di.data.database.entity.AthleteEntity
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity


@Database(
    entities = [AthleteEntity::class, GamesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
    abstract fun athleteDao(): AthleteDao


}
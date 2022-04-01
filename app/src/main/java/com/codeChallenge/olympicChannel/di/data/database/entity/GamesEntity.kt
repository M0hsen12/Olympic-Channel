package com.codeChallenge.olympicChannel.di.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bluelinelabs.logansquare.annotation.JsonField
@Entity(tableName = "games")
class GamesEntity(
    @PrimaryKey
    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "game_id")
    var gameId: Int?= null,

    @ColumnInfo(name = "year")
    var year: Int? = null
)
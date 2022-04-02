package com.codeChallenge.olympicChannel.di.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bluelinelabs.logansquare.annotation.JsonField
import com.codeChallenge.olympicChannel.model.Athlete
import java.util.ArrayList

@Entity(tableName = "games")
class GamesEntity(
    @PrimaryKey
    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "game_id")
    var gameId: Int = -1,

    @ColumnInfo(name = "year")
    var year: Int = -1,

    @ColumnInfo(name = "athletes")
    var athletes: List<Athlete> = ArrayList()
)
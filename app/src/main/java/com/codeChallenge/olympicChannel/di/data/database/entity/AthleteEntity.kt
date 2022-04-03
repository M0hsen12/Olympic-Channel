package com.codeChallenge.olympicChannel.di.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeChallenge.olympicChannel.model.Athlete

@Entity(tableName = "athlete")
class AthleteEntity(
    @PrimaryKey
    @ColumnInfo(name = "athlete_id")
    var athleteId: String = "",

    @ColumnInfo(name = "bio")
    var bio: String? = null,

    @ColumnInfo(name = "dateOfBirth")
    var dateOfBirth: String? = null,

    @ColumnInfo(name = "height")
    var height: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "photo_id")
    var photoId: Int? = null,

    @ColumnInfo(name = "surname")
    var surname: String? = null,

    @ColumnInfo(name = "weight")
    var weight: Int? = null,

    @ColumnInfo(name = "athletes")
    var athletes: List<Athlete>? = null

)
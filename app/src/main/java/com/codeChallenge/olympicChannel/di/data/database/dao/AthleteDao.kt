package com.codeChallenge.olympicChannel.di.data.database.dao

import androidx.room.*
import com.codeChallenge.olympicChannel.di.data.database.entity.AthleteEntity
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import io.reactivex.Single

@Dao
abstract class AthleteDao {

    @Query("SELECT * FROM athlete")
    abstract fun all(): Single<List<AthleteEntity>>

    @Query("SELECT * FROM athlete WHERE athlete_id LIKE :id LIMIT 1")
    abstract fun findByID(id: Int): AthleteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(athlete: AthleteEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(athlete: ArrayList<AthleteEntity>): Single<Unit>

    @Update
    abstract fun update(athlete: AthleteEntity): Single<Unit>

    @Query("DELETE FROM athlete")
    abstract fun deleteAll()
}
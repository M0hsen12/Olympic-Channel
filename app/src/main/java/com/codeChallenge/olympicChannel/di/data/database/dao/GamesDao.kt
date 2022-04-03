package com.codeChallenge.olympicChannel.di.data.database.dao

import androidx.room.*
import com.codeChallenge.olympicChannel.di.data.database.entity.GamesEntity
import io.reactivex.Single

@Dao
abstract class GamesDao {
    @Query("SELECT * FROM games")
    abstract fun all(): List<GamesEntity>

    @Query("SELECT * FROM games WHERE city LIKE :city LIMIT 1")
    abstract fun findByName(city: String): GamesEntity

    @Query("SELECT * FROM games WHERE game_id LIKE :id LIMIT 1")
    abstract fun findByID(id: Int): GamesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(game: GamesEntity):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(games: ArrayList<GamesEntity>)

    @Update
    abstract fun update(game: GamesEntity)

    @Query("DELETE FROM games")
    abstract fun deleteAll()


}
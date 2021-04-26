package com.liridon.fraktontask.db

import androidx.room.*
import com.liridon.fraktontask.model.Place

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(place: Place): Long

    @Query("SELECT * FROM places")
    fun getAllPlaces(): List<Place>

    @Delete
    suspend fun deletePlace(place: Place): Void
}
package com.liridon.db

import androidx.room.*
import com.liridon.fraktontask.model.Place

@Dao
interface PlaceDao {

    @Insert()
    suspend fun insert(place: Place): Void

    @Update
    suspend fun update(place: Place): Void

    @Query("SELECT * FROM places")
    fun getAllPlaces(): List<Place>

    @Delete
    suspend fun deletePlace(place: Place): Void
}
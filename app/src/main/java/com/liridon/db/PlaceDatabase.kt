package com.liridon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liridon.fraktontask.model.Place

@Database(
    entities = [Place::class],
    version = 1
)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun getPlaceDao(): PlaceDao

    companion object{
        @Volatile
        private var instance: PlaceDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PlaceDatabase::class.java,
                "place_db.db"
            ).build()
    }
}
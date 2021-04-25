package com.liridon.fraktontask.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
        tableName = "Places"
)
data class Place (
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        @ColumnInfo(name = "latitude")
        var latitude: Double? = null,
        @ColumnInfo(name = "longitude")
        var longitude: Double? = null
)
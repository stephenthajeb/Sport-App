package com.example.sportapp

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

@Entity(tableName = "history_table")
@Parcelize
data class History(
    var img: Bitmap? = null,
    var mode: String? = null,
    var date: String? = null,
    var startTime: String? = null,
    var duration: Double? = null, //in minutes
    var result: Double? = null,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
): Parcelable

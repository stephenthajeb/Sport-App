package com.example.sportapp.Data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

/*
*  mode : RUNNING or CYCLING
*  date : yyyy-mm-dd format as String
*  startTime,endTime: HH-mm format as String
*  result: if mode == RUNNING result is in steps unit, else it is in km unit
* */


@Entity(tableName = "history_table")
@Parcelize
data class History(
    var mode: String? = null,
    var date: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var result: Float? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable

package com.example.sportapp.Data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/*
    mode: "RUNNING" or "CYCLING"
    frequencyMode: ONCE -> once at a specific time
                  EVERYDAY -> routine every day at a time
                  CUSTOM -> routine et some certain day at a specific time
    date: Date of the workout, for frequency Mode EVERYDAT and CUSTOM, this field will not be used
    days: array of int with each element's value represent the n-th day. 1 -> Sunday, 2 -> Monday. This field won't be used for frequencyMode = 0
    startTime,finishTime:
    target: if mode == running then this field represent value in step unit, if cycling represent value in km unit

 */
@Entity(tableName = "schedule_table")
@Parcelize
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var mode: String? = null,
    var frequency: String? = null,
    var date: String? = null,
    var days: String? = null,
    var startTime: String? = null,
    var finishTime: String? = null,
    var target: Double? = null,
    var isAuto: Int? = null

    //var isAchive: Int = 0
) : Parcelable


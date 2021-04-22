package com.example.sportapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date

/*
    mode: "RUNNING" or "CYCLING"
    frequencyMode: ONCE -> once at a specific time
                  EVERYDAY -> routine every day at a time
                  CUSTOM -> routine et some certain day at a specific time
    date: Date of the workout, for frequency Mode 1 and 2, this field will not be used
    days: array of int with each element's value represent the n-th day. 1 -> Sunday, 2 -> Monday. This field won't be used for frequencyMode = 0
    startTime,finishTime: 24 hour format. 4 digit number 1234 indicate 12:34
    target: if mode == running then this field represent value in step unit, if cycling represent value in km unit

 */

@Parcelize
data class Schedule(
    var mode: String? = null,//RUNNING
    var frequencyMode: String? = null,
    var date: Date? = null,
    var days:List<Int> = emptyList(),
    var startTime:Long? = null,
    var finishTime:Long? = null,
    var target: Double? = null,
    var isAuto: Boolean? = null
): Parcelable

package com.example.sportapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date

/*
    mode: "RUNNING" or "CYCLING"
    frequencyMode: 0 -> once at a specific time
               1 -> routine every day at a time
               2 -> routine et some certain day at a specific time
    date: Date of the workout, for frequency Mode 1 and 2, this field will not be used
    days: array of int with each element's value represent the n-th day. 1 -> Sunday, 2 -> Monday. This field won't be used for frequencyMode = 0
    startTime,finishTime: 24 hour format. 4 digit number 1234 indicate 12:34
    target: if mode == running then this field represent value in step unit, if cycling represent value in km unit

 */

@Parcelize
data class Schedule(
    val mode: String?,//RUNNING
    val frequencyMode: Int,
    val date: Date?,
    val days:List<Int>,
    val startTime:Long,
    val finishTime:Long,
    val target: Double,
    val isAuto: Boolean
): Parcelable

package com.example.sportapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val mode: String?,
    val frequency: Int,
    val days:List<Int>,
    val startTime:Long,//24 hour format. 4 digit number 1234 indicate 12:34
    val finishTime:Long,
): Parcelable

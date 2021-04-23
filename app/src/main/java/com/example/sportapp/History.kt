package com.example.sportapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

@Parcelize
data class History(
    var mode: String? = null,
    var date: Date? = null,
    var startTime: Time? = null,
    var duration: Double? = null, //in minutes
    var result: Double? = null
): Parcelable

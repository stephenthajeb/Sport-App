package com.example.sportapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
   val title:String?,
   val url: String?,
   val img: String?,
   val description: String?,
   val source: String?,
   val author: String?,
   val date: String?,
): Parcelable

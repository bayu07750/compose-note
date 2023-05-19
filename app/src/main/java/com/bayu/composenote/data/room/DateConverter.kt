package com.bayu.composenote.data.room

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromDateToTime(date: Date): Long = date.time
    @TypeConverter
    fun fromTimeToDate(time: Long): Date = Date(time)
}
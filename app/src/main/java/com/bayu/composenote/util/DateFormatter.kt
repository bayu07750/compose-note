package com.bayu.composenote.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private const val DEFAULT_FORMATTER = "d MMM yyyy"

    fun format(time: Long, pattern: String = DEFAULT_FORMATTER): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).run {
            format(Date(time)).orEmpty()
        }
    }

    fun toDate(date: String, pattern: String = DEFAULT_FORMATTER): Date {
        return SimpleDateFormat(pattern, Locale.getDefault()).run {
            parse(date) ?: Date()
        }
    }
}
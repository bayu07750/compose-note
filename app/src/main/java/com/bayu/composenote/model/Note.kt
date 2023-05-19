package com.bayu.composenote.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(
    val id: Int,
    val name: String,
    val title: String,
    val description: String,
    val dateStart: Date,
    val dateEnd: Date,
) {

    fun formattedDateStart(): String {
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(dateStart).orEmpty()
    }

    fun formattedDateEnd(): String {
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(dateEnd).orEmpty()
    }
}
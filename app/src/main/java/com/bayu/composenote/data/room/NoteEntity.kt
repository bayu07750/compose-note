package com.bayu.composenote.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val title: String,
    val description: String,
    val dateStart: Date,
    val dateEnd: Date,
)
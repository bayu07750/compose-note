package com.bayu.composenote

import com.bayu.composenote.data.room.NoteEntity
import java.util.Date

object TestUtils {
    val dummyNote = NoteEntity(
        id = 1,
        name = "Jetpack Compose",
        title = "Jetpack Compose",
        description = "UI TOOLKIT FOR ANDROID",
        dateStart = Date(),
        dateEnd = Date()
    )

    fun generateDummyNotes(): List<NoteEntity> = List(10) {
        val id = it + 1
        NoteEntity(
            id = id,
            name = "name - $id",
            title = "title - $id",
            description = "description - $id",
            dateStart = Date(System.currentTimeMillis() + id),
            dateEnd = Date(System.currentTimeMillis() + id)
        )
    }
}
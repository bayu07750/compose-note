package com.bayu.composenote.data.mapper

import com.bayu.composenote.data.room.NoteEntity
import com.bayu.composenote.model.Note

fun NoteEntity.mapToUI() =
    Note(
        id = id,
        name = name,
        title = title,
        description = description,
        dateStart = dateStart,
        dateEnd = dateEnd
    )

fun Note.mapToEntity() =
    NoteEntity(id = id, name = name, title = title, description = description, dateStart = dateStart, dateEnd = dateEnd)
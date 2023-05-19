package com.bayu.composenote.data.repository

import com.bayu.composenote.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note
    suspend fun addUpdateNote(note: Note)
}
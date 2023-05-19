package com.bayu.composenote.data.repository

import com.bayu.composenote.data.mapper.mapToEntity
import com.bayu.composenote.data.mapper.mapToUI
import com.bayu.composenote.data.room.NoteDao
import com.bayu.composenote.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImp @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> = noteDao
        .getAllNotes()
        .flowOn(Dispatchers.IO)
        .map {
            it.map { note -> note.mapToUI() }
        }
        .flowOn(Dispatchers.Default)

    override suspend fun getNoteById(id: Int): Note {
        return withContext(Dispatchers.IO) {noteDao.getNoteById(id).mapToUI() }
    }

    override suspend fun addUpdateNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.addUpdate(note.mapToEntity())
        }
    }
}
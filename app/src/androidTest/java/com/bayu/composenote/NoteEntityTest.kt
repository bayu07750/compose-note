package com.bayu.composenote

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bayu.composenote.data.room.NoteDao
import com.bayu.composenote.data.room.NoteDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class NoteEntityTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()
        noteDao = db.noteDao()
    }

    @Throws(IOException::class)
    @After
    fun tearDown() {
        db.close()
    }

    @Throws(IOException::class)
    @Test
    fun addNote() = runTest {
        noteDao.addUpdate(TestUtils.dummyNote)
        val byId = noteDao.getNoteById(TestUtils.dummyNote.id)
        Assert.assertEquals(TestUtils.dummyNote, byId)
    }

    @Throws(IOException::class)
    @Test
    fun deleteNote() = runTest {
        noteDao.addUpdate(TestUtils.dummyNote)
        val byId = noteDao.getNoteById(TestUtils.dummyNote.id)
        noteDao.delete(byId)
        val note = noteDao.getNoteById(byId.id)
        Assert.assertNull(note)
    }

    @Throws(IOException::class)
    @Test
    fun getAllNotes() = runTest {
        val dummyNotes = TestUtils.generateDummyNotes()
        dummyNotes.forEach {
            noteDao.addUpdate(it)
        }
        // reversed because get all notes order by DESC
        val notes = noteDao.getAllNotes().first().reversed()
        Assert.assertEquals(dummyNotes.size, notes.size)
        Assert.assertEquals(dummyNotes.first(), notes.first())
        Assert.assertEquals(dummyNotes.last(), notes.last())
    }
}
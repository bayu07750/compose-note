package com.bayu.composenote.data.di

import android.content.Context
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.bayu.composenote.data.repository.NoteRepository
import com.bayu.composenote.data.repository.NoteRepositoryImp
import com.bayu.composenote.data.room.NoteDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = NoteDatabase.getInstance(context.applicationContext)

    @Singleton
    @Provides
    fun provideNoteDao(db: NoteDatabase) = db.noteDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideNoteRepository(imp: NoteRepositoryImp): NoteRepository
}
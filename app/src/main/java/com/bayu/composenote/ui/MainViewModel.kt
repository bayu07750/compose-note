package com.bayu.composenote.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bayu.composenote.data.repository.NoteRepository
import com.bayu.composenote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    fun addUpdateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addUpdateNote(note)
        }
    }
}
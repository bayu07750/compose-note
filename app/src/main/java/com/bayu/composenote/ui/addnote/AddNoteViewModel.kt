package com.bayu.composenote.ui.addnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bayu.composenote.data.repository.NoteRepository
import com.bayu.composenote.model.Note
import com.bayu.composenote.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddNoteUiState(
    val added: Boolean,
    val note: Note? = null,
)

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddNoteUiState(false))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val noteId = savedStateHandle.get<Int>("id") ?: -1
            if (noteId != -1) {
                getNoteById(noteId)
            }
        }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(id)
            _uiState.update { it.copy(note = note) }
        }
    }

    fun addNote(title: String, description: String, startDate: String, endDate: String, updateState: Boolean = true) {
        viewModelScope.launch {
            val id = _uiState.value.note?.id
            noteRepository.addUpdateNote(
                Note(
                    id = id ?: 0,
                    name = title,
                    title = title,
                    description = description,
                    dateStart = DateFormatter.toDate(startDate),
                    dateEnd = DateFormatter.toDate(endDate)
                )
            )

            if (updateState) {
                _uiState.update { it.copy(added = true) }
            } else {
                _uiState.update { it.copy(note = null, added = false) }
            }
        }
    }
}
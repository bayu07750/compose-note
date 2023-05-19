package com.bayu.composenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bayu.composenote.data.repository.NoteRepository
import com.bayu.composenote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val shouldShowBottomSheet: Boolean,
    val selectedNote: Note? = null,
    val notes: List<Note> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(false))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes().collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun onNoteClicked(note: Note) {
        _uiState.update {
            it.copy(shouldShowBottomSheet = true, selectedNote = note)
        }
    }

    fun onDismissBottomSheet() {
        _uiState.update {
            it.copy(shouldShowBottomSheet = false, selectedNote = null)
        }
    }
}

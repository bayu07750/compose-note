package com.bayu.composenote.ui.addnote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberAddNoteState(uiState: AddNoteUiState?) = remember(uiState) {
    AddNoteState(uiState)
}

class AddNoteState(state: AddNoteUiState?) {

    var title by mutableStateOf(state?.note?.title.orEmpty())
    var description by mutableStateOf(state?.note?.description.orEmpty())
    var startDate by mutableStateOf(state?.note?.formattedDateStart().orEmpty())
    var endDate by mutableStateOf(state?.note?.formattedDateEnd().orEmpty())
    val allowAddNote by derivedStateOf {
        title.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()
    }
}
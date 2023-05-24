@file:OptIn(ExperimentalMaterial3Api::class)

package com.bayu.composenote.ui.addnote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bayu.composenote.ui.theme.ComposeNoteTheme
import com.bayu.composenote.util.DateFormatter
import com.bayu.jctypealias.JCallback
import com.bayu.jctypealias.JCallbackString
import com.bayu.jctypealias.JCallbackType4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    state: AddNoteUiState,
    onBack: JCallback,
    onAddNote: JCallbackType4<String, String, String, String>,
    modifier: Modifier = Modifier,
) {
    val addNoteState = rememberAddNoteState(uiState = state)

    if (state.added) {
        LaunchedEffect(Unit) {
            onBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add note")
                },
                actions = {
                    IconButton(onClick = {
                        onAddNote(
                            addNoteState.title,
                            addNoteState.description,
                            addNoteState.startDate,
                            addNoteState.endDate
                        )
                    }, enabled = addNoteState.allowAddNote) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "Add note")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        AddNoteContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            addNoteState = addNoteState
        )
    }
}

@Composable
fun AddNoteContent(
    modifier: Modifier = Modifier,
    addNoteState: AddNoteState = rememberAddNoteState(null)
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp),
    ) {
        OutlinedTextField(
            value = addNoteState.title,
            onValueChange = { addNoteState.title = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Title")
            }
        )
        OutlinedTextField(
            value = addNoteState.description,
            onValueChange = { addNoteState.description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            label = {
                Text(text = "Description")
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            SelectedDateButton(
                label = "Select start date",
                date = addNoteState.startDate,
                onSelectedDate = { addNoteState.startDate = it },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            SelectedDateButton(
                label = "Select end date",
                date = addNoteState.endDate,
                onSelectedDate = { addNoteState.endDate = it },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedDateButton(
    label: String,
    date: String,
    onSelectedDate: JCallbackString,
    modifier: Modifier = Modifier,
) {
    var openDialog by remember { mutableStateOf(false) }
    OutlinedButton(
        onClick = { openDialog = true },
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ) {
        Text(text = date.ifEmpty { label })
    }

    if (openDialog) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog = false
                    onSelectedDate(DateFormatter.format(datePickerState.selectedDateMillis ?: 0))
                }, enabled = confirmEnabled) {
                    Text(text = "Select")
                }
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddNoteScreen() {
    ComposeNoteTheme {
        AddNoteScreen(
            state = AddNoteUiState(added = false),
            onBack = {},
            onAddNote = { s: String, s1: String, s2: String, s3: String -> },
        )
    }
}
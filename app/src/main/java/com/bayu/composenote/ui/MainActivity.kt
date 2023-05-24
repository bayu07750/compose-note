package com.bayu.composenote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bayu.composenote.model.Note
import com.bayu.composenote.ui.addnote.AddNoteContent
import com.bayu.composenote.ui.addnote.AddNoteViewModel
import com.bayu.composenote.ui.addnote.rememberAddNoteState
import com.bayu.composenote.ui.home.HomeScreen
import com.bayu.composenote.ui.home.HomeUiState
import com.bayu.composenote.ui.home.HomeViewModel
import com.bayu.composenote.ui.home.NoteDateItem
import com.bayu.composenote.ui.theme.ComposeNoteTheme
import com.bayu.composenote.util.DateFormatter
import com.bayu.jctypealias.JCallback
import com.bayu.jctypealias.JCallbackType
import dagger.hilt.android.AndroidEntryPoint

val LocalWindowSizeClass = compositionLocalOf { WindowWidthSizeClass.Compact }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNoteTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass.widthSizeClass) {
                    if (LocalWindowSizeClass.current == WindowWidthSizeClass.Compact) {
                        AppGraph()
                    } else {
                        AppLargeScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLargeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf(false) }
    val addNoteViewModel = hiltViewModel<AddNoteViewModel>()
    val addNoteUiState by addNoteViewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreen(
            uiState = uiState,
            onNoteClicked = viewModel::onNoteClicked,
            onAddNoteBtnClicked = { openDialog = true },
            onDismissBottomSheet = viewModel::onDismissBottomSheet,
            onEditNoteClicked = { /* TODO */ },
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F),
                    shape = CircleShape
                )
                .width(2.dp)
                .fillMaxHeight()
        )
        DetailScreen(
            uiState = uiState,
            modifier = Modifier.weight(2f),
            onCloseButtonClicked = {
                viewModel.onDismissBottomSheet()
            },
            onEditButtonClicked = {
                addNoteViewModel.getNoteById(it.id)
                openDialog = true
            },
        )
    }

    if (openDialog) {
        val addNoteState = rememberAddNoteState(uiState = addNoteUiState)

        AlertDialog(onDismissRequest = { openDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Text(
                            text = "Add note",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                                .alignByBaseline()
                        )
                        IconButton(onClick = { openDialog = false }, modifier = Modifier.alignByBaseline()) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
                        }
                    }
                    AddNoteContent(
                        modifier = Modifier,
                        addNoteState = addNoteState,
                    )
                    Button(onClick = {
                        addNoteViewModel.addNote(
                            addNoteState.title,
                            addNoteState.description,
                            addNoteState.startDate,
                            addNoteState.endDate,
                            false
                        )
                        addNoteUiState.note?.copy(
                            name = addNoteState.title,
                            description = addNoteState.description,
                            dateStart = DateFormatter.toDate(addNoteState.startDate),
                            dateEnd = DateFormatter.toDate(addNoteState.endDate),
                            title = addNoteState.title
                        )?.let {
                            viewModel.onNoteClicked(
                                it
                            )
                        }
                        openDialog = false
                    }, modifier = Modifier.align(Alignment.End), enabled = addNoteState.allowAddNote) {
                        Text(text = "Add note")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: HomeUiState,
    onCloseButtonClicked: JCallback,
    onEditButtonClicked: JCallbackType<Note>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (uiState.shouldShowBottomSheet && uiState.selectedNote != null) {
                TopAppBar(
                    title = {
                        Text(text = uiState.selectedNote.title)
                    },
                    actions = {
                        IconButton(onClick = { onEditButtonClicked.invoke(uiState.selectedNote) }) {
                            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit note")
                        }
                        IconButton(onClick = {
                            onCloseButtonClicked.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close note"
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        val innerPadModifier = Modifier.padding(innerPadding)
        if (uiState.shouldShowBottomSheet && uiState.selectedNote != null) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(state = scrollState),
            ) {
                val note = uiState.selectedNote!!
                Text(text = note.title.uppercase(), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = note.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                NoteDateItem(
                    dateStart = note.formattedDateStart(),
                    dateEnd = note.formattedDateEnd()
                )
            }
        } else {
            Box(
                modifier = innerPadModifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select note to see details",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

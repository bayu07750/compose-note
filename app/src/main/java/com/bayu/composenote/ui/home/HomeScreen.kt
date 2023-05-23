@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.bayu.composenote.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bayu.composenote.R
import com.bayu.composenote.model.Note
import com.bayu.composenote.ui.LocalWindowSizeClass
import com.bayu.composenote.ui.theme.ComposeNoteTheme
import com.bayu.jctypealias.JCallback
import com.bayu.jctypealias.JCallbackType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNoteClicked: JCallbackType<Note>,
    onAddNoteBtnClicked: JCallback,
    onDismissBottomSheet: JCallback,
    onEditNoteClicked: JCallbackType<Note>,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    BottomSheetScaffold(
        uiState = uiState,
        sheetState = sheetState,
        onAddNoteBtnClicked = onAddNoteBtnClicked,
        onEditNoteClicked = onEditNoteClicked,
        onDismissBottomSheet = onDismissBottomSheet,
        modifier = modifier,
    ) { innerPadding ->
        val innerPadModifier = Modifier.padding(innerPadding)
        when {
            uiState.notes.isEmpty() -> {
                Column(
                    modifier = innerPadModifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.empty))
                }
            }

            else -> {
                Notes(
                    onNoteClicked = onNoteClicked,
                    modifier = innerPadModifier.fillMaxSize(),
                    notes = uiState.notes,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffold(
    uiState: HomeUiState,
    sheetState: SheetState,
    onAddNoteBtnClicked: JCallback,
    onEditNoteClicked: JCallbackType<Note>,
    onDismissBottomSheet: JCallback,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = "Add note")
                },
                icon = { Icon(imageVector = Icons.Rounded.NoteAdd, contentDescription = null) },
                onClick = onAddNoteBtnClicked,
            )
        }
    ) { innerPadding ->
        content.invoke(innerPadding)
    }

    if (uiState.shouldShowBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissBottomSheet,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            ) {
                val note = uiState.selectedNote
                IconButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissBottomSheet.invoke()
                        }
                        if (note != null) {
                            onEditNoteClicked(note)
                        }
                    }
                }, modifier = Modifier.align(Alignment.End)) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
                Text(text = note?.title?.uppercase().orEmpty(), style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = note?.description.toString(), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                NoteDateItem(
                    dateStart = note?.formattedDateStart().toString(),
                    dateEnd = note?.formattedDateEnd().toString()
                )
            }
        }
    }
}

@Composable
fun Notes(
    onNoteClicked: JCallbackType<Note>,
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = notes,
            key = { it.id }
        ) { note ->
            NoteItem(
                name = note.title,
                description = note.description,
                onClickItem = {
                    onNoteClicked(note)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    name: String,
    description: String,
    onClickItem: JCallback,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClickItem
    ) {
        Column(
            modifier = Modifier
                .padding(all = 24.dp)
                .then(modifier)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name.uppercase(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun NoteDateItem(
    dateStart: String,
    dateEnd: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DateItem(date = dateStart)
        val color = MaterialTheme.colorScheme.onSurface
        Canvas(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(8.dp)
                .height(1.dp)
        ) {
            drawRect(color = color)
        }
        DateItem(date = dateEnd)
    }
}

@Composable
fun DateItem(
    date: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
    ) {
        val shouldOnePanel = LocalWindowSizeClass.current == WindowWidthSizeClass.Compact
        Text(
            text = date,
            style = if (shouldOnePanel) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(all = 4.dp)
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewNoteItem() {
    ComposeNoteTheme {
        NoteItem(
            name = "Testing",
            description = "Testing",
            onClickItem = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
package com.bayu.composenote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bayu.composenote.R
import com.bayu.composenote.ui.home.HomeViewModel
import com.bayu.composenote.ui.home.NoteDateItem
import com.bayu.composenote.ui.home.Notes
import com.bayu.composenote.ui.theme.ComposeNoteTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalWindowSizeClass = compositionLocalOf { WindowWidthSizeClass.Compact }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNoteTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                val shouldShowOnPanel = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass.widthSizeClass) {
                    if (shouldShowOnPanel) {
                        AppGraph()
                    } else {
                        val viewModel = hiltViewModel<HomeViewModel>()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Scaffold(
                                modifier = Modifier.weight(1f),
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
                                        onClick = { /* TODO */ },
                                    )
                                }
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
                                            onNoteClicked = {
                                                viewModel.onNoteClicked(it)
                                            },
                                            modifier = innerPadModifier.fillMaxSize(),
                                            notes = uiState.notes,
                                        )
                                    }
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F),
                                        shape = CircleShape
                                    )
                                    .width(2.dp)
                                    .fillMaxHeight()
                            )
                            Scaffold(
                                modifier = Modifier.weight(2F),
                                topBar = {
                                    if (uiState.shouldShowBottomSheet && uiState.selectedNote != null) {
                                        TopAppBar(
                                            title = {
                                                Text(text = uiState.selectedNote!!.title)
                                            },
                                            actions = {
                                                IconButton(onClick = { /*TODO*/ }) {
                                                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit note")
                                                }
                                                IconButton(onClick = {
                                                    viewModel.onDismissBottomSheet()
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
                    }
                }
            }
        }
    }
}

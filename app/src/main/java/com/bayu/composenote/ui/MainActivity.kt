package com.bayu.composenote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.bayu.composenote.ui.theme.ComposeNoteTheme
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

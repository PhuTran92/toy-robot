package com.example.toyrobot.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

private const val DEBOUNCE_INTERVAL_MS = 150L

/**
 * Returns a debounced version of [onClick] that ignores calls within
 * [DEBOUNCE_INTERVAL_MS] milliseconds of the previous invocation.
 *
 * Uses [rememberUpdatedState] so the latest [onClick] lambda is always
 * invoked even if the caller's lambda instance changes between recompositions.
 */
@Composable
fun debounced(onClick: () -> Unit): () -> Unit {
    val currentOnClick = rememberUpdatedState(onClick)
    val lastClickTime = remember { mutableLongStateOf(0L) }
    return remember {
        {
            val now = System.currentTimeMillis()
            if (now - lastClickTime.longValue >= DEBOUNCE_INTERVAL_MS) {
                lastClickTime.longValue = now
                currentOnClick.value()
            }
        }
    }
}


package com.example.toyrobot.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.toyrobot.R


@Composable
fun ActionButtonsLayout(
    enabled: Boolean,
    onMove: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onReport: () -> Unit
) {
    val spacing = dimensionResource(R.dimen.action_buttons_spacing)
    val buttonHeight = dimensionResource(R.dimen.action_button_height)
    val debouncedReport = debounced(onReport)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            listOf(
                stringResource(R.string.button_move) to debounced(onMove),
                stringResource(R.string.button_left) to debounced(onLeft),
                stringResource(R.string.button_right) to debounced(onRight),
            ).forEach { (label, action) ->
                Button(
                    onClick = action,
                    enabled = enabled,
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight)
                ) {
                    Text(label)
                }
            }
        }
        Button(
            onClick = debouncedReport,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
        ) {
            Text(stringResource(R.string.button_report))
        }
    }
}

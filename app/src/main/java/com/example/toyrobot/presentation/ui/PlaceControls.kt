package com.example.toyrobot.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.toyrobot.R
import com.example.toyrobot.domain.model.Direction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceControls(onPlace: (Int, Int, Direction) -> Unit) {
    var xInput by rememberSaveable { mutableStateOf("") }
    var yInput by rememberSaveable { mutableStateOf("") }
    var selectedDirection by rememberSaveable { mutableStateOf(Direction.NORTH) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.place_controls_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.place_controls_spacing), Alignment.CenterVertically)
    ) {
        Text(stringResource(R.string.label_place))
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.place_controls_spacing)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CoordinateTextField(
                value = xInput,
                onValueChange = { xInput = it },
                label = stringResource(R.string.label_x),
                modifier = Modifier.weight(1f)
            )
            CoordinateTextField(
                value = yInput,
                onValueChange = { yInput = it },
                label = stringResource(R.string.label_y),
                modifier = Modifier.weight(1f)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1.6f)
            ) {
                OutlinedTextField(
                    value = selectedDirection.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_facing)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Direction.entries.forEach { dir ->
                        DropdownMenuItem(
                            text = {
                                Text(dir.name)
                            },
                            onClick = {
                                selectedDirection = dir
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(
                onClick = debounced {
                    val x = xInput.toIntOrNull()
                    val y = yInput.toIntOrNull()
                    if (x != null && y != null) {
                        onPlace(x, y, selectedDirection)
                    }
                },
                enabled = xInput.isNotBlank() && yInput.isNotBlank()
            ) {
                Text(stringResource(R.string.button_go))
            }
        }
    }
}

@Composable
private fun CoordinateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true
    )
}

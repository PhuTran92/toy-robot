package com.example.toyrobot.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.example.toyrobot.R
import com.example.toyrobot.presentation.ui.ActionButtonsLayout
import com.example.toyrobot.presentation.ui.CommandLogLayout
import com.example.toyrobot.presentation.ui.PlaceControls
import com.example.toyrobot.presentation.ui.TableGridLayout
import com.example.toyrobot.presentation.viewmodel.RobotViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToyRobotScreen(
    viewModel: RobotViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.top_bar_title))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 5×5 Grid
            TableGridLayout(
                robot = uiState.robot,
                modifier = Modifier.weight(2f)
            )

            // PLACE Controls
            PlaceControls(onPlace = { x, y, dir -> viewModel.place(x, y, dir) })

            // Action Buttons
            ActionButtonsLayout(
                enabled = uiState.isPlaced,
                onMove = { viewModel.move() },
                onLeft = { viewModel.turnLeft() },
                onRight = { viewModel.turnRight() },
                onReport = { viewModel.report() }
            )

            // Command Log
            CommandLogLayout(
                log = uiState.commandLog,
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClear = { viewModel.clearLog() }
            )
        }
    }
}
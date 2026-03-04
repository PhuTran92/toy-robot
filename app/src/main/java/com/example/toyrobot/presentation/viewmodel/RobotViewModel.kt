package com.example.toyrobot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.usecase.MoveRobotUseCase
import com.example.toyrobot.domain.usecase.PlaceRobotUseCase
import com.example.toyrobot.domain.usecase.ReportRobotUseCase
import com.example.toyrobot.domain.usecase.TurnLeftUseCase
import com.example.toyrobot.domain.usecase.TurnRightUseCase
import com.example.toyrobot.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RobotViewModel(
    private val placeRobotUseCase: PlaceRobotUseCase,
    private val moveRobotUseCase: MoveRobotUseCase,
    private val turnLeftUseCase: TurnLeftUseCase,
    private val turnRightUseCase: TurnRightUseCase,
    private val reportRobotUseCase: ReportRobotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViewState())
    val uiState: StateFlow<ViewState> = _uiState.asStateFlow()

    fun place(x: Int, y: Int, direction: Direction) {
        val (newRobot, success) = placeRobotUseCase(_uiState.value.robot, x, y, direction)
        _uiState.update { state ->
            ViewState(
                robot = newRobot,
                isPlaced = newRobot != null,
                commandLog = state.commandLog + "PLACE $x,$y,${direction.name}${if (!success) " (ignored)" else ""}"
            )
        }
    }

    fun move() {
        val (newRobot, success) = moveRobotUseCase(_uiState.value.robot)
        _uiState.update { state ->
            ViewState(
                robot = newRobot,
                isPlaced = newRobot != null,
                commandLog = state.commandLog + "MOVE${if (!success) " (ignored)" else ""}"
            )
        }
    }

    fun turnLeft() {
        val (newRobot, success) = turnLeftUseCase(_uiState.value.robot)
        _uiState.update { state ->
            ViewState(
                robot = newRobot,
                isPlaced = newRobot != null,
                commandLog = state.commandLog + "LEFT${if (!success) " (ignored)" else ""}"
            )
        }
    }

    fun turnRight() {
        val (newRobot, success) = turnRightUseCase(_uiState.value.robot)
        _uiState.update { state ->
            ViewState(
                robot = newRobot,
                isPlaced = newRobot != null,
                commandLog = state.commandLog + "RIGHT${if (!success) " (ignored)" else ""}"
            )
        }
    }

    fun report() {
        val reportText = reportRobotUseCase(_uiState.value.robot) ?: "(no robot placed)"
        _uiState.update { state ->
            state.copy(commandLog = state.commandLog + "REPORT → $reportText")
        }
    }

    fun clearLog() {
        _uiState.update { it.copy(commandLog = emptyList()) }
    }
}
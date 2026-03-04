package com.example.toyrobot.presentation.viewmodel

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import com.example.toyrobot.domain.model.RobotResult
import com.example.toyrobot.domain.usecase.MoveRobotUseCase
import com.example.toyrobot.domain.usecase.PlaceRobotUseCase
import com.example.toyrobot.domain.usecase.ReportRobotUseCase
import com.example.toyrobot.domain.usecase.TurnLeftUseCase
import com.example.toyrobot.domain.usecase.TurnRightUseCase
import com.example.toyrobot.presentation.ViewState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RobotViewModelTest {

    private lateinit var placeRobotUseCase: PlaceRobotUseCase
    private lateinit var moveRobotUseCase: MoveRobotUseCase
    private lateinit var turnLeftUseCase: TurnLeftUseCase
    private lateinit var turnRightUseCase: TurnRightUseCase
    private lateinit var reportRobotUseCase: ReportRobotUseCase
    private lateinit var viewModel: RobotViewModel

    @Before
    fun setUp() {
        placeRobotUseCase = mockk()
        moveRobotUseCase = mockk()
        turnLeftUseCase = mockk()
        turnRightUseCase = mockk()
        reportRobotUseCase = mockk()
        viewModel = RobotViewModel(
            placeRobotUseCase,
            moveRobotUseCase,
            turnLeftUseCase,
            turnRightUseCase,
            reportRobotUseCase
        )
    }

    // region Initial state

    @Test
    fun `initial uiState has no robot and empty log`() {
        val state = viewModel.uiState.value

        assertNull(state.robot)
        assertFalse(state.isPlaced)
        assertTrue(state.commandLog.isEmpty())
    }

    // endregion

    // region place()

    @Test
    fun `place with valid position updates robot and logs command`() {
        // Given
        val robot = Robot(1, 2, Direction.NORTH)
        every { placeRobotUseCase(null, 1, 2, Direction.NORTH) } returns RobotResult(robot, true)

        // When
        viewModel.place(1, 2, Direction.NORTH)

        // Then
        val state = viewModel.uiState.value
        assertEquals(robot, state.robot)
        assertTrue(state.isPlaced)
        assertEquals("PLACE 1,2,NORTH", state.commandLog.last())
    }

    @Test
    fun `place with invalid position keeps null robot and logs ignored`() {
        // Given
        every { placeRobotUseCase(null, 5, 5, Direction.NORTH) } returns RobotResult(null, false)

        // When
        viewModel.place(5, 5, Direction.NORTH)

        // Then
        val state = viewModel.uiState.value
        assertNull(state.robot)
        assertFalse(state.isPlaced)
        assertEquals("PLACE 5,5,NORTH (ignored)", state.commandLog.last())
    }

    @Test
    fun `place appends to existing command log`() {
        // Given
        val robot1 = Robot(0, 0, Direction.EAST)
        val robot2 = Robot(2, 3, Direction.SOUTH)
        every { placeRobotUseCase(null, 0, 0, Direction.EAST) } returns RobotResult(robot1, true)
        every { placeRobotUseCase(robot1, 2, 3, Direction.SOUTH) } returns RobotResult(robot2, true)

        // When
        viewModel.place(0, 0, Direction.EAST)
        viewModel.place(2, 3, Direction.SOUTH)

        // Then
        val log = viewModel.uiState.value.commandLog
        assertEquals(2, log.size)
        assertEquals("PLACE 0,0,EAST", log[0])
        assertEquals("PLACE 2,3,SOUTH", log[1])
    }

    // endregion

    // region move()

    @Test
    fun `move with placed robot updates position and logs command`() {
        // Given
        val initialRobot = Robot(2, 2, Direction.NORTH)
        val movedRobot = Robot(2, 3, Direction.NORTH)
        every { placeRobotUseCase(null, 2, 2, Direction.NORTH) } returns RobotResult(initialRobot, true)
        every { moveRobotUseCase(initialRobot) } returns RobotResult(movedRobot, true)
        viewModel.place(2, 2, Direction.NORTH)

        // When
        viewModel.move()

        // Then
        val state = viewModel.uiState.value
        assertEquals(movedRobot, state.robot)
        assertEquals("MOVE", state.commandLog.last())
    }

    @Test
    fun `move at table edge keeps position and logs ignored`() {
        // Given
        val edgeRobot = Robot(2, 4, Direction.NORTH)
        every { placeRobotUseCase(null, 2, 4, Direction.NORTH) } returns RobotResult(edgeRobot, true)
        every { moveRobotUseCase(edgeRobot) } returns RobotResult(edgeRobot, false)
        viewModel.place(2, 4, Direction.NORTH)

        // When
        viewModel.move()

        // Then
        val state = viewModel.uiState.value
        assertEquals(edgeRobot, state.robot)
        assertEquals("MOVE (ignored)", state.commandLog.last())
    }

    @Test
    fun `move without placed robot logs ignored`() {
        // Given
        every { moveRobotUseCase(null) } returns RobotResult(null, false)

        // When
        viewModel.move()

        // Then
        val state = viewModel.uiState.value
        assertNull(state.robot)
        assertFalse(state.isPlaced)
        assertEquals("MOVE (ignored)", state.commandLog.last())
    }

    // endregion

    // region turnLeft()

    @Test
    fun `turnLeft updates facing and logs command`() {
        // Given
        val initialRobot = Robot(2, 2, Direction.NORTH)
        val turnedRobot = Robot(2, 2, Direction.WEST)
        every { placeRobotUseCase(null, 2, 2, Direction.NORTH) } returns RobotResult(initialRobot, true)
        every { turnLeftUseCase(initialRobot) } returns RobotResult(turnedRobot, true)
        viewModel.place(2, 2, Direction.NORTH)

        // When
        viewModel.turnLeft()

        // Then
        val state = viewModel.uiState.value
        assertEquals(Direction.WEST, state.robot?.facing)
        assertEquals("LEFT", state.commandLog.last())
    }

    @Test
    fun `turnLeft without placed robot logs ignored`() {
        // Given
        every { turnLeftUseCase(null) } returns RobotResult(null, false)

        // When
        viewModel.turnLeft()

        // Then
        val state = viewModel.uiState.value
        assertNull(state.robot)
        assertEquals("LEFT (ignored)", state.commandLog.last())
    }

    // endregion

    // region turnRight()

    @Test
    fun `turnRight updates facing and logs command`() {
        // Given
        val initialRobot = Robot(2, 2, Direction.NORTH)
        val turnedRobot = Robot(2, 2, Direction.EAST)
        every { placeRobotUseCase(null, 2, 2, Direction.NORTH) } returns RobotResult(initialRobot, true)
        every { turnRightUseCase(initialRobot) } returns RobotResult(turnedRobot, true)
        viewModel.place(2, 2, Direction.NORTH)

        // When
        viewModel.turnRight()

        // Then
        val state = viewModel.uiState.value
        assertEquals(Direction.EAST, state.robot?.facing)
        assertEquals("RIGHT", state.commandLog.last())
    }

    @Test
    fun `turnRight without placed robot logs ignored`() {
        // Given
        every { turnRightUseCase(null) } returns RobotResult(null, false)

        // When
        viewModel.turnRight()

        // Then
        val state = viewModel.uiState.value
        assertNull(state.robot)
        assertEquals("RIGHT (ignored)", state.commandLog.last())
    }

    // endregion

    // region report()

    @Test
    fun `report with placed robot logs position string`() {
        // Given
        val robot = Robot(3, 3, Direction.NORTH)
        every { placeRobotUseCase(null, 3, 3, Direction.NORTH) } returns RobotResult(robot, true)
        every { reportRobotUseCase(robot) } returns "3,3,NORTH"
        viewModel.place(3, 3, Direction.NORTH)

        // When
        viewModel.report()

        // Then
        assertEquals("REPORT → 3,3,NORTH", viewModel.uiState.value.commandLog.last())
    }

    @Test
    fun `report without placed robot logs no robot placed`() {
        // Given
        every { reportRobotUseCase(null) } returns null

        // When
        viewModel.report()

        // Then
        assertEquals("REPORT → (no robot placed)", viewModel.uiState.value.commandLog.last())
    }

    // endregion

    // region clearLog()

    @Test
    fun `clearLog empties the command log`() {
        // Given
        val robot = Robot(0, 0, Direction.EAST)
        every { placeRobotUseCase(null, 0, 0, Direction.EAST) } returns RobotResult(robot, true)
        every { moveRobotUseCase(robot) } returns RobotResult(robot, false)
        viewModel.place(0, 0, Direction.EAST)
        viewModel.move()
        assertTrue(viewModel.uiState.value.commandLog.isNotEmpty())

        // When
        viewModel.clearLog()

        // Then
        assertTrue(viewModel.uiState.value.commandLog.isEmpty())
    }

    @Test
    fun `clearLog preserves robot state`() {
        // Given
        val robot = Robot(1, 1, Direction.SOUTH)
        every { placeRobotUseCase(null, 1, 1, Direction.SOUTH) } returns RobotResult(robot, true)
        viewModel.place(1, 1, Direction.SOUTH)

        // When
        viewModel.clearLog()

        // Then
        val state = viewModel.uiState.value
        assertEquals(robot, state.robot)
        assertTrue(state.isPlaced)
        assertTrue(state.commandLog.isEmpty())
    }

    // endregion

    // region combined flows

    @Test
    fun `full sequence place move report produces correct log`() {
        // Given
        val placed = Robot(0, 0, Direction.NORTH)
        val moved = Robot(0, 1, Direction.NORTH)
        every { placeRobotUseCase(null, 0, 0, Direction.NORTH) } returns RobotResult(placed, true)
        every { moveRobotUseCase(placed) } returns RobotResult(moved, true)
        every { reportRobotUseCase(moved) } returns "0,1,NORTH"

        // When
        viewModel.place(0, 0, Direction.NORTH)
        viewModel.move()
        viewModel.report()

        // Then
        val log = viewModel.uiState.value.commandLog
        assertEquals(3, log.size)
        assertEquals("PLACE 0,0,NORTH", log[0])
        assertEquals("MOVE", log[1])
        assertEquals("REPORT → 0,1,NORTH", log[2])
    }

    // endregion
}


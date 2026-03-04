package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Constants
import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MoveRobotUseCaseTest {

    private lateinit var validatePosition: ValidatePositionUseCase
    private lateinit var moveRobot: MoveRobotUseCase

    @Before
    fun setUp() {
        validatePosition = mockk()
        moveRobot = MoveRobotUseCase(validatePosition)
    }

    @Test
    fun `move when no robot placed returns failure`() {
        // Given
        val robot = null

        // When
        val result = moveRobot(robot)

        // Then
        assertFalse(result.success)
        assertNull(result.robot)
    }

    @Test
    fun `move NORTH increments y by 1`() {
        // Given
        val robot = Robot(2, 2, Direction.NORTH)
        every { validatePosition(2, 3) } returns true

        // When
        val result = moveRobot(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(2, 3, Direction.NORTH), result.robot)
    }

    @Test
    fun `move SOUTH decrements y by 1`() {
        // Given
        val robot = Robot(2, 2, Direction.SOUTH)
        every { validatePosition(2, 1) } returns true

        // When
        val result = moveRobot(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(2, 1, Direction.SOUTH), result.robot)
    }

    @Test
    fun `move EAST increments x by 1`() {
        // Given
        val robot = Robot(2, 2, Direction.EAST)
        every { validatePosition(3, 2) } returns true

        // When
        val result = moveRobot(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(3, 2, Direction.EAST), result.robot)
    }

    @Test
    fun `move WEST decrements x by 1`() {
        // Given
        val robot = Robot(2, 2, Direction.WEST)
        every { validatePosition(1, 2) } returns true

        // When
        val result = moveRobot(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(1, 2, Direction.WEST), result.robot)
    }

    @Test
    fun `move NORTH from top edge returns failure and keeps position`() {
        // Given
        val robot = Robot(2, Constants.TABLE_SIZE - 1, Direction.NORTH)
        every { validatePosition(2, Constants.TABLE_SIZE) } returns false

        // When
        val result = moveRobot(robot)

        // Then
        assertFalse(result.success)
        assertEquals(robot, result.robot)
    }

    @Test
    fun `move SOUTH from bottom edge returns failure and keeps position`() {
        // Given
        val robot = Robot(2, 0, Direction.SOUTH)
        every { validatePosition(2, -1) } returns false

        // When
        val result = moveRobot(robot)

        // Then
        assertFalse(result.success)
        assertEquals(robot, result.robot)
    }

    @Test
    fun `move EAST from right edge returns failure and keeps position`() {
        // Given
        val robot = Robot(Constants.TABLE_SIZE - 1, 2, Direction.EAST)
        every { validatePosition(Constants.TABLE_SIZE, 2) } returns false

        // When
        val result = moveRobot(robot)

        // Then
        assertFalse(result.success)
        assertEquals(robot, result.robot)
    }

    @Test
    fun `move WEST from left edge returns failure and keeps position`() {
        // Given
        val robot = Robot(0, 2, Direction.WEST)
        every { validatePosition(-1, 2) } returns false

        // When
        val result = moveRobot(robot)

        // Then
        assertFalse(result.success)
        assertEquals(robot, result.robot)
    }

    @Test
    fun `move does not change facing direction`() {
        // Given
        val robot = Robot(1, 1, Direction.EAST)
        every { validatePosition(2, 1) } returns true

        // When
        val result = moveRobot(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.EAST, result.robot?.facing)
    }
}

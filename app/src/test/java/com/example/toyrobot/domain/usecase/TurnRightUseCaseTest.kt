package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TurnRightUseCaseTest {

    private lateinit var turnRight: TurnRightUseCase

    @Before
    fun setUp() {
        turnRight = TurnRightUseCase()
    }

    @Test
    fun `turn right when no robot placed returns failure`() {
        // Given
        val robot = null

        // When
        val result = turnRight(robot)

        // Then
        assertFalse(result.success)
        assertNull(result.robot)
    }

    @Test
    fun `turn right from NORTH faces EAST`() {
        // Given
        val robot = Robot(2, 2, Direction.NORTH)

        // When
        val result = turnRight(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.EAST, result.robot?.facing)
    }

    @Test
    fun `turn right from EAST faces SOUTH`() {
        // Given
        val robot = Robot(2, 2, Direction.EAST)

        // When
        val result = turnRight(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.SOUTH, result.robot?.facing)
    }

    @Test
    fun `turn right from SOUTH faces WEST`() {
        // Given
        val robot = Robot(2, 2, Direction.SOUTH)

        // When
        val result = turnRight(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.WEST, result.robot?.facing)
    }

    @Test
    fun `turn right from WEST faces NORTH`() {
        // Given
        val robot = Robot(2, 2, Direction.WEST)

        // When
        val result = turnRight(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.NORTH, result.robot?.facing)
    }

    @Test
    fun `turn right does not change robot position`() {
        // Given
        val robot = Robot(1, 3, Direction.SOUTH)

        // When
        val result = turnRight(robot)

        // Then
        assertTrue(result.success)
        assertEquals(1, result.robot?.x)
        assertEquals(3, result.robot?.y)
    }
}

package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TurnLeftUseCaseTest {

    private lateinit var turnLeft: TurnLeftUseCase

    @Before
    fun setUp() {
        turnLeft = TurnLeftUseCase()
    }

    @Test
    fun `turn left when no robot placed returns failure`() {
        // Given
        val robot = null

        // When
        val result = turnLeft(robot)

        // Then
        assertFalse(result.success)
        assertNull(result.robot)
    }

    @Test
    fun `turn left from NORTH faces WEST`() {
        // Given
        val robot = Robot(2, 2, Direction.NORTH)

        // When
        val result = turnLeft(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.WEST, result.robot?.facing)
    }

    @Test
    fun `turn left from WEST faces SOUTH`() {
        // Given
        val robot = Robot(2, 2, Direction.WEST)

        // When
        val result = turnLeft(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.SOUTH, result.robot?.facing)
    }

    @Test
    fun `turn left from SOUTH faces EAST`() {
        // Given
        val robot = Robot(2, 2, Direction.SOUTH)

        // When
        val result = turnLeft(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.EAST, result.robot?.facing)
    }

    @Test
    fun `turn left from EAST faces NORTH`() {
        // Given
        val robot = Robot(2, 2, Direction.EAST)

        // When
        val result = turnLeft(robot)

        // Then
        assertTrue(result.success)
        assertEquals(Direction.NORTH, result.robot?.facing)
    }

    @Test
    fun `turn left does not change robot position`() {
        // Given
        val robot = Robot(3, 4, Direction.NORTH)

        // When
        val result = turnLeft(robot)

        // Then
        assertTrue(result.success)
        assertEquals(3, result.robot?.x)
        assertEquals(4, result.robot?.y)
    }
}

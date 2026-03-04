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

class PlaceRobotUseCaseTest {

    private lateinit var validatePosition: ValidatePositionUseCase
    private lateinit var placeRobot: PlaceRobotUseCase

    @Before
    fun setUp() {
        validatePosition = mockk()
        placeRobot = PlaceRobotUseCase(validatePosition)
    }

    @Test
    fun `valid placement returns success with correct robot state`() {
        // Given
        every { validatePosition(2, 3) } returns true

        // When
        val result = placeRobot(null, 2, 3, Direction.NORTH)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(2, 3, Direction.NORTH), result.robot)
    }

    @Test
    fun `placement at origin returns success`() {
        // Given
        every { validatePosition(0, 0) } returns true

        // When
        val result = placeRobot(null, 0, 0, Direction.EAST)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(0, 0, Direction.EAST), result.robot)
    }

    @Test
    fun `placement at max corner returns success`() {
        // Given
        val maxSize = Constants.TABLE_SIZE - 1
        every { validatePosition(maxSize, maxSize) } returns true

        // When
        val result = placeRobot(null, maxSize, maxSize, Direction.SOUTH)

        // Then
        assertTrue(result.success)
        assertEquals(Robot(maxSize, maxSize, Direction.SOUTH), result.robot)
    }

    @Test
    fun `out of bounds placement returns failure with null robot when no current robot`() {
        // Given
        every { validatePosition(Constants.TABLE_SIZE, 0) } returns false

        // When
        val result = placeRobot(null, Constants.TABLE_SIZE, 0, Direction.NORTH)

        // Then
        assertFalse(result.success)
        assertNull(result.robot)
    }

    @Test
    fun `out of bounds placement returns failure with unchanged current robot`() {
        // Given
        val current = Robot(1, 1, Direction.WEST)
        every { validatePosition(-1, 2) } returns false

        // When
        val result = placeRobot(current, -1, 2, Direction.NORTH)

        // Then
        assertFalse(result.success)
        assertEquals(current, result.robot)
    }

    @Test
    fun `negative coordinates return failure`() {
        // Given
        every { validatePosition(-1, -1) } returns false

        // When
        val result = placeRobot(null, -1, -1, Direction.NORTH)

        // Then
        assertFalse(result.success)
        assertNull(result.robot)
    }
}

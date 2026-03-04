package com.example.toyrobot.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DirectionTest {

    // --- turnLeft ---

    @Test
    fun `turnLeft from NORTH returns WEST`() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft())
    }

    @Test
    fun `turnLeft from WEST returns SOUTH`() {
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft())
    }

    @Test
    fun `turnLeft from SOUTH returns EAST`() {
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft())
    }

    @Test
    fun `turnLeft from EAST returns NORTH`() {
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft())
    }

    @Test
    fun `turnLeft four times returns original direction`() {
        // Given
        val original = Direction.NORTH

        // When
        val result = original.turnLeft().turnLeft().turnLeft().turnLeft()

        // Then
        assertEquals(original, result)
    }

    // --- turnRight ---

    @Test
    fun `turnRight from NORTH returns EAST`() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight())
    }

    @Test
    fun `turnRight from EAST returns SOUTH`() {
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight())
    }

    @Test
    fun `turnRight from SOUTH returns WEST`() {
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight())
    }

    @Test
    fun `turnRight from WEST returns NORTH`() {
        assertEquals(Direction.NORTH, Direction.WEST.turnRight())
    }

    @Test
    fun `turnRight four times returns original direction`() {
        // Given
        val original = Direction.SOUTH

        // When
        val result = original.turnRight().turnRight().turnRight().turnRight()

        // Then
        assertEquals(original, result)
    }

    @Test
    fun `turnLeft then turnRight returns original direction`() {
        Direction.entries.forEach { direction ->
            assertEquals(direction, direction.turnLeft().turnRight())
        }
    }

    @Test
    fun `turnRight then turnLeft returns original direction`() {
        Direction.entries.forEach { direction ->
            assertEquals(direction, direction.turnRight().turnLeft())
        }
    }
}

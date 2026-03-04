package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ReportRobotUseCaseTest {

    private lateinit var reportRobot: ReportRobotUseCase

    @Before
    fun setUp() {
        reportRobot = ReportRobotUseCase()
    }

    @Test
    fun `report when no robot placed returns null`() {
        // Given
        val robot = null

        // When
        val result = reportRobot(robot)

        // Then
        assertNull(result)
    }

    @Test
    fun `report returns correct format for NORTH`() {
        // Given
        val robot = Robot(3, 3, Direction.NORTH)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("3,3,NORTH", result)
    }

    @Test
    fun `report returns correct format for EAST`() {
        // Given
        val robot = Robot(1, 2, Direction.EAST)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("1,2,EAST", result)
    }

    @Test
    fun `report returns correct format for SOUTH`() {
        // Given
        val robot = Robot(0, 4, Direction.SOUTH)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("0,4,SOUTH", result)
    }

    @Test
    fun `report returns correct format for WEST`() {
        // Given
        val robot = Robot(4, 0, Direction.WEST)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("4,0,WEST", result)
    }

    @Test
    fun `report at origin facing NORTH returns correct string`() {
        // Given
        val robot = Robot(0, 0, Direction.NORTH)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("0,0,NORTH", result)
    }

    @Test
    fun `report at max position returns correct string`() {
        // Given
        val robot = Robot(4, 4, Direction.SOUTH)

        // When
        val result = reportRobot(robot)

        // Then
        assertEquals("4,4,SOUTH", result)
    }
}

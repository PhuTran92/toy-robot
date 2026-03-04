package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Constants
import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePositionUseCaseTest {

    private lateinit var validatePosition: ValidatePositionUseCase

    @Before
    fun setUp() {
        validatePosition = ValidatePositionUseCase()
    }

    @Test
    fun `valid position at origin returns true`() {
        assertTrue(validatePosition(0, 0))
    }

    @Test
    fun `valid position at max corner returns true`() {
        val maxSize = Constants.TABLE_SIZE - 1
        assertTrue(validatePosition(maxSize, maxSize))
    }

    @Test
    fun `valid position in middle of table returns true`() {
        assertTrue(validatePosition(2, 3))
    }

    @Test
    fun `negative x returns false`() {
        assertFalse(validatePosition(-1, 0))
    }

    @Test
    fun `negative y returns false`() {
        assertFalse(validatePosition(0, -1))
    }

    @Test
    fun `x equal to TABLE_SIZE returns false`() {
        assertFalse(validatePosition(Constants.TABLE_SIZE, 0))
    }

    @Test
    fun `y equal to TABLE_SIZE returns false`() {
        assertFalse(validatePosition(0, Constants.TABLE_SIZE))
    }

    @Test
    fun `both x and y out of bounds returns false`() {
        assertFalse(validatePosition(Constants.TABLE_SIZE, Constants.TABLE_SIZE))
    }

    @Test
    fun `all valid positions on the table pass validation`() {
        val maxSize = Constants.TABLE_SIZE - 1
        for (x in 0 until maxSize) {
            for (y in 0 until maxSize) {
                assertTrue("Expected ($x,$y) to be valid", validatePosition(x, y))
            }
        }
    }
}

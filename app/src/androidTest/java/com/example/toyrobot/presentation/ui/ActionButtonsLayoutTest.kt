package com.example.toyrobot.presentation.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.toyrobot.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActionButtonsLayoutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // region Visibility

    @Test
    fun allButtonsAreDisplayed() {
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = {},
                onLeft = {},
                onRight = {},
                onReport = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).assertExists()
    }

    // endregion

    // region Enabled state

    @Test
    fun allButtonsAreEnabledWhenEnabledIsTrue() {
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = {},
                onLeft = {},
                onRight = {},
                onReport = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).assertIsEnabled()
    }

    @Test
    fun allButtonsAreDisabledWhenEnabledIsFalse() {
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = false,
                onMove = {},
                onLeft = {},
                onRight = {},
                onReport = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).assertIsNotEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).assertIsNotEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).assertIsNotEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).assertIsNotEnabled()
    }

    // endregion

    // region Click callbacks

    @Test
    fun clickingMoveButtonInvokesOnMove() {
        // Given
        val onMove: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = onMove,
                onLeft = {},
                onRight = {},
                onReport = {}
            )
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).performClick()

        // Then
        verify(exactly = 1) { onMove() }
    }

    @Test
    fun clickingLeftButtonInvokesOnLeft() {
        // Given
        val onLeft: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = {},
                onLeft = onLeft,
                onRight = {},
                onReport = {}
            )
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).performClick()

        // Then
        verify(exactly = 1) { onLeft() }
    }

    @Test
    fun clickingRightButtonInvokesOnRight() {
        // Given
        val onRight: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = {},
                onLeft = {},
                onRight = onRight,
                onReport = {}
            )
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).performClick()

        // Then
        verify(exactly = 1) { onRight() }
    }

    @Test
    fun clickingReportButtonInvokesOnReport() {
        // Given
        val onReport: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = true,
                onMove = {},
                onLeft = {},
                onRight = {},
                onReport = onReport
            )
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).performClick()

        // Then
        verify(exactly = 1) { onReport() }
    }

    @Test
    fun clickingButtonsWhenDisabledDoesNotInvokeCallbacks() {
        // Given
        val onMove: () -> Unit = mockk(relaxed = true)
        val onLeft: () -> Unit = mockk(relaxed = true)
        val onRight: () -> Unit = mockk(relaxed = true)
        val onReport: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            ActionButtonsLayout(
                enabled = false,
                onMove = onMove,
                onLeft = onLeft,
                onRight = onRight,
                onReport = onReport
            )
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).performClick()

        // Then
        verify(exactly = 0) { onMove() }
        verify(exactly = 0) { onLeft() }
        verify(exactly = 0) { onRight() }
        verify(exactly = 0) { onReport() }
    }

    // endregion
}

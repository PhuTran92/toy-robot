package com.example.toyrobot.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.toyrobot.R
import com.example.toyrobot.presentation.ui.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // region Initial screen state

    @Test
    fun topBarTitleIsDisplayed() {
        composeTestRule.onNodeWithText(context.getString(R.string.top_bar_title))
            .assertIsDisplayed()
    }

    @Test
    fun placeControlsAreDisplayedOnLaunch() {
        composeTestRule.onNodeWithText(context.getString(R.string.label_place)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsDisplayed()
    }

    @Test
    fun goButtonIsDisabledOnLaunch() {
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsNotEnabled()
    }

    // endregion

    // region PLACE command

    @Test
    fun goButtonBecomesEnabledAfterEnteringXAndY() {
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("1")
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("2")

        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsEnabled()
    }

    @Test
    fun placingRobotEnablesActionButtons() {
        placeRobotAt(x = "1", y = "2")

        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).assertIsEnabled()
    }

    @Test
    fun placingRobotAppendsPlaceEntryToCommandLog() {
        placeRobotAt(x = "1", y = "2")

        logEntry("PLACE 1,2,NORTH").assertIsDisplayed()
    }

    @Test
    fun placingRobotOutOfBoundsAppendsIgnoredToCommandLog() {
        placeRobotAt(x = "9", y = "9")

        logEntry("PLACE 9,9,NORTH (ignored)").assertIsDisplayed()
    }

    // endregion

    // region MOVE command

    @Test
    fun movingRobotAppendsEntryToCommandLog() {
        // Given
        placeRobotAt(x = "2", y = "2")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).performClick()

        // Then
        logEntry("MOVE").assertIsDisplayed()
    }

    @Test
    fun movingRobotAtEdgeAppendsIgnoredToCommandLog() {
        // Given — place at top edge
        placeRobotAt(x = "0", y = "4")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).performClick()

        // Then
        logEntry("MOVE (ignored)").assertIsDisplayed()
    }

    // endregion

    // region TURN LEFT / TURN RIGHT commands

    @Test
    fun turningLeftAppendsEntryToCommandLog() {
        // Given
        placeRobotAt(x = "2", y = "2")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_left)).performClick()

        // Then
        logEntry("LEFT").assertIsDisplayed()
    }

    @Test
    fun turningRightAppendsEntryToCommandLog() {
        // Given
        placeRobotAt(x = "2", y = "2")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_right)).performClick()

        // Then
        logEntry("RIGHT").assertIsDisplayed()
    }

    // endregion

    // region REPORT command

    @Test
    fun reportAppendsPositionStringToCommandLog() {
        // Given
        placeRobotAt(x = "3", y = "3")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_report)).performClick()

        // Then
        logEntry("REPORT → 3,3,NORTH").assertIsDisplayed()
    }

    // endregion

    // region Command log — clear

    @Test
    fun clearButtonRemovesAllLogEntries() {
        // Given
        placeRobotAt(x = "1", y = "1")
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).performClick()

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_clear)).performClick()

        // Then
        composeTestRule.onNodeWithText(context.getString(R.string.log_empty)).assertIsDisplayed()
    }

    @Test
    fun clearButtonDoesNotRemoveRobotState() {
        // Given
        placeRobotAt(x = "2", y = "2")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_clear)).performClick()

        // Then — action buttons should still be enabled (robot is still placed)
        composeTestRule.onNodeWithText(context.getString(R.string.button_move)).assertIsEnabled()
    }

    // endregion

    // region Helpers

    private fun placeRobotAt(x: String, y: String) {
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput(x)
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput(y)
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).performClick()
    }

    /**
     * Finds a text node that is a descendant of the CommandLog container.
     * This avoids ambiguous matches when the same text appears in both a button
     * label (e.g. "MOVE", "LEFT", "RIGHT") and a log entry simultaneously.
     */
    private fun logEntry(text: String) = composeTestRule.onNode(
        hasText(text) and hasAnyAncestor(hasTestTag(TestTags.COMMAND_LOG))
    )

    // endregion
}

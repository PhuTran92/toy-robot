package com.example.toyrobot.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
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
class CommandLogLayoutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // region Empty state

    @Test
    fun emptyMessageIsShownWhenLogIsEmpty() {
        composeTestRule.setContent {
            CommandLogLayout(log = emptyList(), onClear = {})
        }

        composeTestRule.onNodeWithText(context.getString(R.string.log_empty)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.button_clear)).assertIsDisplayed()
    }

    // endregion

    // region Log entries

    @Test
    fun multipleLogEntriesAreDisplayed() {
        composeTestRule.setContent {
            CommandLogLayout(
                log = listOf("PLACE 0,0,NORTH", "MOVE", "LEFT", "REPORT → 0,1,WEST"),
                onClear = {}
            )
        }

        composeTestRule.onNodeWithText("PLACE 0,0,NORTH").assertIsDisplayed()
        composeTestRule.onNodeWithText("MOVE").assertIsDisplayed()
        composeTestRule.onNodeWithText("LEFT").assertIsDisplayed()
        composeTestRule.onNodeWithText("REPORT → 0,1,WEST").assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.log_empty)).assertDoesNotExist()
    }

    // endregion

    // region Clear callback

    @Test
    fun clickingClearButtonInvokesOnClear() {
        // Given
        val onClear: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            CommandLogLayout(log = listOf("MOVE"), onClear = onClear)
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_clear)).performClick()

        // Then
        verify(exactly = 1) { onClear() }
    }

    // endregion
}

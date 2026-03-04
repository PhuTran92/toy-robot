package com.example.toyrobot.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.toyrobot.R
import com.example.toyrobot.domain.model.Direction
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaceControlsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // region Visibility

    @Test
    fun placeControlsAreDisplayed() {
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        composeTestRule.onNodeWithText(context.getString(R.string.label_place)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_facing)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsDisplayed()
    }

    @Test
    fun northIsShownAsDefaultDirection() {
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        composeTestRule.onNodeWithText(Direction.NORTH.name).assertIsDisplayed()
    }

    // endregion

    // region GO button enabled state

    @Test
    fun goButtonIsDisabledWhenBothFieldsAreEmpty() {
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsNotEnabled()
    }

    @Test
    fun goButtonIsDisabledWhenOnlyXIsEntered() {
        // Given
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("2")

        // Then
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsNotEnabled()
    }

    @Test
    fun goButtonIsDisabledWhenOnlyYIsEntered() {
        // Given
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("3")

        // Then
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsNotEnabled()
    }

    @Test
    fun goButtonIsEnabledWhenBothXAndYAreEntered() {
        // Given
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("1")
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("2")

        // Then
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).assertIsEnabled()
    }

    // endregion

    // region GO button callback

    @Test
    fun clickingGoWithValidInputsInvokesOnPlace() {
        // Given
        val onPlace: (Int, Int, Direction) -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PlaceControls(onPlace = onPlace)
        }
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("1")
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("2")

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).performClick()

        // Then
        verify(exactly = 1) { onPlace(1, 2, Direction.NORTH) }
    }

    @Test
    fun onPlaceReceivesCorrectCoordinatesAndDefaultDirection() {
        // Given
        val onPlace: (Int, Int, Direction) -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PlaceControls(onPlace = onPlace)
        }

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("3")
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("4")
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).performClick()

        // Then
        verify { onPlace(3, 4, Direction.NORTH) }
    }

    // endregion

    // region Direction dropdown

    @Test
    fun selectingDirectionFromDropdownUpdatesDisplayedValue() {
        // Given
        composeTestRule.setContent {
            PlaceControls(onPlace = { _, _, _ -> })
        }

        // When
        composeTestRule.onNodeWithText(Direction.NORTH.name).performClick()
        composeTestRule.onNodeWithText(Direction.EAST.name).performClick()

        // Then
        composeTestRule.onNodeWithText(Direction.EAST.name).assertIsDisplayed()
    }

    @Test
    fun selectedDirectionIsPassedToOnPlace() {
        // Given
        val onPlace: (Int, Int, Direction) -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PlaceControls(onPlace = onPlace)
        }
        composeTestRule.onNodeWithText(context.getString(R.string.label_x)).performTextInput("0")
        composeTestRule.onNodeWithText(context.getString(R.string.label_y)).performTextInput("0")
        composeTestRule.onNodeWithText(Direction.NORTH.name).performClick()
        composeTestRule.onNodeWithText(Direction.SOUTH.name).performClick()

        // When
        composeTestRule.onNodeWithText(context.getString(R.string.button_go)).performClick()

        // Then
        verify { onPlace(0, 0, Direction.SOUTH) }
    }

    // endregion
}

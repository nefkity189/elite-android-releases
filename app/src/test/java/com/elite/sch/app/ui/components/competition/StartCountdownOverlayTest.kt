package com.elite.sch.app.ui.components.competition

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.elite.sch.app.ui.theme.EliteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class StartCountdownOverlayTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `renders 3 state`() {
        composeRule.setContent { EliteTheme { StartCountdownOverlay(3) } }
        composeRule.onNodeWithTag("start.n3").assertExists()
        composeRule.onNodeWithText("3").assertExists()
    }

    @Test
    fun `renders 2 state`() {
        composeRule.setContent { EliteTheme { StartCountdownOverlay(2) } }
        composeRule.onNodeWithTag("start.n2").assertExists()
        composeRule.onNodeWithText("2").assertExists()
    }

    @Test
    fun `renders 1 state`() {
        composeRule.setContent { EliteTheme { StartCountdownOverlay(1) } }
        composeRule.onNodeWithTag("start.n1").assertExists()
        composeRule.onNodeWithText("1").assertExists()
    }

    @Test
    fun `renders the go state`() {
        composeRule.setContent {
            EliteTheme { StartCountdownOverlay(0) }
        }

        composeRule.onNodeWithTag("start.go").assertExists()
        composeRule.onNodeWithText("JÁ!").assertExists()
    }
}

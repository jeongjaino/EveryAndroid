package com.example.composelayouttest.AndroidTest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.composelayouttest.MainActivity
import com.example.composelayouttest.R
import com.example.composelayouttest.ui.theme.ComposeLayoutTestTheme
import org.junit.Rule
import org.junit.Test

class MyComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun counter_initally_zero(){
        composeTestRule.onNodeWithText("clicks : 0").assertExists()
    }
    @Test
    fun clickButton_incrementsCounter(){
        val textIncrement = composeTestRule.activity.getString(R.string.Increment_counter)
        composeTestRule.onNodeWithText(textIncrement).performClick()

        val textClicks = composeTestRule.activity.getString(R.string.click, 1)
        composeTestRule.onNodeWithText(textClicks).assertExists()
    }
}
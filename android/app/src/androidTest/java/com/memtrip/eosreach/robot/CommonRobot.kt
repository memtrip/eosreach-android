package com.memtrip.eosreach.robot

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class CommonRobot {

    fun clickDialogOk(): CommonRobot {
        onView(withText("OK"))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun pressBack(): CommonRobot {
        Espresso.pressBack()
        return this
    }

    fun pressHomeUp(): CommonRobot {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
package com.memtrip.eosreach.robot.account.balance

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.memtrip.eosreach.R

class ActionsRobot {

    fun selectTransferButton(): ActionsRobot {
        onView(ViewMatchers.withId(R.id.account_actions_transfer_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
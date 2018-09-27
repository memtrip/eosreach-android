package com.memtrip.eosreach.robot.transaction

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.memtrip.eosreach.R

class TransactionRobot {

    fun selectDoneButton(): TransactionRobot {
        onView(withId(R.id.transaction_receipt_done_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
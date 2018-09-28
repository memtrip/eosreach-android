package com.memtrip.eosreach.robot.transaction

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class TransactionRobot {

    fun selectDoneButton(): TransactionRobot {
        onView(withId(R.id.transaction_receipt_done_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyTransactionErrorDialog(): TransactionRobot {
        onView(withText(R.string.transaction_view_log_position_button))
            .check(matches(isDisplayed()))
        onView(withText(R.string.transaction_view_log_negative_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectViewLogButton(): TransactionRobot {
        onView(withText(R.string.transaction_view_log_position_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyTransactionLog(log: String): TransactionRobot {
        onView(withId(R.id.transaction_log_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.transaction_log_data))
            .check(matches(isDisplayed()))
            .check(matches(withText(log)))
        return this
    }
}
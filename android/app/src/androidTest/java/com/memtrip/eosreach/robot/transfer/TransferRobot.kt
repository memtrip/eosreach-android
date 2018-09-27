package com.memtrip.eosreach.robot.transfer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class TransferRobot {

    fun enterRecipient(recipient: String): TransferRobot {
        onView(withId(R.id.transfer_form_to_input))
            .check(matches(isDisplayed()))
            .perform(typeText(recipient))
            .perform(pressImeActionButton())
        return this
    }

    fun enterAmount(amount: String): TransferRobot {
        onView(withId(R.id.transfer_form_amount_input))
            .check(matches(isDisplayed()))
            .perform(typeText(amount))
            .perform(pressImeActionButton())
        return this
    }

    fun enterMemo(memo: String): TransferRobot {
        onView(withId(R.id.transfer_form_memo_input))
            .check(matches(isDisplayed()))
            .perform(typeText(memo))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectNextButton(): TransferRobot {
        onView(withId(R.id.transfer_form_next_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyAmount(amount: String): TransferRobot {
        onView(withId(R.id.transfer_details_amount_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(amount)))
        return this
    }

    fun verifyTo(to: String): TransferRobot {
        onView(withId(R.id.transfer_details_to_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(to)))
        return this
    }

    fun verifyFrom(from: String): TransferRobot {
        onView(withId(R.id.transfer_details_from_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(from)))
        return this
    }

    fun verifyMemo(memo: String): TransferRobot {
        onView(withId(R.id.transfer_details_memo_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(memo)))
        return this
    }

    fun selectConfirmButton(): TransferRobot {
        onView(withId(R.id.transfer_confirm_confirm_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
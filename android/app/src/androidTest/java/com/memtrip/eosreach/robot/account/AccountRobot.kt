package com.memtrip.eosreach.robot.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class AccountRobot {

    fun verifyAccountSuccess(): AccountRobot {

        onView(withId(R.id.account_toolbar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.account_balance_background))
            .check(matches(isDisplayed()))

        return this
    }

    fun verifyAvailableBalance(): AccountRobot {

        onView(withId(R.id.account_available_balance_value))
            .check(matches(isDisplayed()))
            .check(matches(withText("$162947.12")))

        onView(withId(R.id.account_available_balance_label))
            .check(matches(isDisplayed()))
            .check(matches(withText("Available Balance")))

        return this
    }

    fun verifyUnavailableBalance(): AccountRobot {

        onView(withId(R.id.account_available_balance_value))
            .check(matches(isDisplayed()))
            .check(matches(withText("-")))

        onView(withId(R.id.account_available_balance_label))
            .check(matches(isDisplayed()))
            .check(matches(withText("market rates are unavailable")))

        return this
    }

    fun verifyAccountError(): AccountRobot {

        onView(withId(R.id.account_error_view))
            .check(matches(isDisplayed()))

        return this
    }

    fun selectAccountErrorRetry(): AccountRobot {

        onView(withId(R.id.view_error_composite_retry))
            .check(matches(isDisplayed()))
            .perform(click())

        return this
    }

    fun swipeToRefresh(): AccountRobot {

        onView(withId(R.id.account_swipelayout))
            .check(matches(isDisplayed()))
            .perform(swipeDown())

        return this
    }

    fun showErrorDialog(): AccountRobot {

        onView(withText(R.string.account_error_get_account_title))
            .check(matches(isDisplayed()))

        onView(withText(R.string.account_error_get_account_body))
            .check(matches(isDisplayed()))

        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))

        return this
    }

    fun selectVoteTab(): AccountRobot {
        onView(withText(R.string.account_page_vote))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
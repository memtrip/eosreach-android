/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
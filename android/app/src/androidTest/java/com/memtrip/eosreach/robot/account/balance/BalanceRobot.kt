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
package com.memtrip.eosreach.robot.account.balance

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions

import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import org.hamcrest.Matchers.not

class BalanceRobot {

    fun verifyBalanceScreen(): BalanceRobot {
        onView(withId(R.id.balance_airdrop_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyTokenTitle(): BalanceRobot {
        onView(withId(R.id.balance_token_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun hiddenTokenTitle(): BalanceRobot {
        onView(withId(R.id.balance_token_title))
            .check(matches(not(isDisplayed())))
        return this
    }

    fun verifySysBalanceRow(): BalanceRobot {
        onView(withId(R.id.balance_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("SYS")))))
            .check(matches(atPosition(0, hasDescendant(withText("151.0000")))))
        return this
    }

    fun verifyEdnaBalanceRow(): BalanceRobot {
        onView(withId(R.id.balance_list_recyclerview))
            .check(matches(atPosition(1, hasDescendant(withText("EDNA")))))
            .check(matches(atPosition(1, hasDescendant(withText("20.0000")))))
        return this
    }

    fun selectAirDropButton(): BalanceRobot {
        onView(withId(R.id.balance_airdrop_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyLoadingAirDrop(): BalanceRobot {
        onView(withId(R.id.balance_airdrop_progress_text))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyAirDropError(): BalanceRobot {
        onView(withText(R.string.balance_tokens_airdrop_generic_error))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyEmptyAirDrop(): BalanceRobot {
        onView(withText(R.string.balance_tokens_no_airdrops))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyEmptyBalanceMessage(): BalanceRobot {
        onView(withId(R.id.balance_empty))
            .check(matches(isDisplayed()))
        onView(withId(R.id.balance_create_account))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectFirstTokenRow(): BalanceRobot {
        onView(withId(R.id.balance_list_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<SimpleAdapterViewHolder<AccountEntity>>(0, click()))
        return this
    }
}
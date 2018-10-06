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
package com.memtrip.eosreach.robot.accountlist

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import org.hamcrest.Matchers.allOf

class AccountNavigationRobot {

    fun selectNavigationIcon(): AccountNavigationRobot {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click());
        return this
    }

    fun verifyAccountNavigationScreen(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_import_key))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_navigation_create_account))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_navigation_settings))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_navigation_spacer))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_navigation_accounts_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_navigation_refresh_accounts))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectImportKeyNavigationItem(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_import_key))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectCreateAccountNavigationItem(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_create_account))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectSettingsNavigationItem(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_settings))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectRefreshButton(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_refresh_accounts))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyFirstAccountRow(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_accounts_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("memtripissue")))))
            .check(matches(atPosition(0, hasDescendant(withText("151.0000 SYS")))))
        return this
    }

    fun selectFirstAccountRow(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_accounts_recyclerview))
            .perform(actionOnItemAtPosition<SimpleAdapterViewHolder<AccountEntity>>(0, click()))
        return this
    }

    fun verifySecondAccountRow(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_accounts_recyclerview))
            .check(matches(atPosition(1, hasDescendant(withText("memtripissu3")))))
            .check(matches(atPosition(1, hasDescendant(withText("151.0000 SYS")))))
        return this
    }

    fun verifyAccountListError(): AccountNavigationRobot {
        onView(withId(R.id.account_navigation_accounts_error_container))
            .check(matches(isDisplayed()))
        onView(withId(R.id.account_list_error_view))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectAccountErrorRetry(): AccountNavigationRobot {
        onView(allOf(
            withId(R.id.view_error_composite_retry),
            isDescendantOfA(withId(R.id.account_list_error_view))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
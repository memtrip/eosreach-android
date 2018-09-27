package com.memtrip.eosreach.robot.accountlist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder

class AccountListRobot {

    fun verifyAccountListScreen(): AccountListRobot {
        onView(withId(R.id.account_list_toolbar))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectRefreshButton(): AccountListRobot {
        onView(withId(R.id.accounts_list_menu_refresh_accounts))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyFirstAccountRow(): AccountListRobot {
        onView(withId(R.id.account_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("memtripissue")))))
            .check(matches(atPosition(0, hasDescendant(withText("151.0 SYS")))))
        return this
    }

    fun selectFirstAccountRow(): AccountListRobot {
        onView(withId(R.id.account_list_recyclerview))
            .perform(actionOnItemAtPosition<SimpleAdapterViewHolder<AccountEntity>>(0, click()))
        return this
    }

    fun verifySecondAccountRow(): AccountListRobot {
        onView(withId(R.id.account_list_recyclerview))
            .check(matches(atPosition(1, hasDescendant(withText("memtripissu3")))))
            .check(matches(atPosition(1, hasDescendant(withText("151.0 SYS")))))
        return this
    }
}
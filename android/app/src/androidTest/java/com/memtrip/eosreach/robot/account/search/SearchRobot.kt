package com.memtrip.eosreach.robot.account.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.memtrip.eosreach.R
import org.hamcrest.CoreMatchers.allOf

class SearchRobot {

    fun verifySearchScreen(): SearchRobot {
        onView(withId(R.id.explore_search_input_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.explore_search_instruction_label))
            .check(matches(isDisplayed()))
        return this
    }

    fun typeAccountName(accountName: String): SearchRobot {
        onView(allOf(
            withId(R.id.uikit_search_input_view_edittext),
            isDescendantOfA(withId(R.id.explore_search_input_view))
        ))
            .check(matches(isDisplayed()))
            .perform(typeText(accountName))
            .perform(closeSoftKeyboard())
        return this
    }

    fun verifySearchError(): SearchRobot {
        onView(withId((R.id.explore_search_input_account_error)))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectSearchErrorRetry(): SearchRobot {
        onView(allOf(
            withId(R.id.view_error_composite_retry),
            isDescendantOfA(withId(R.id.explore_search_input_account_error))
        )).check(matches(isDisplayed())).perform(click())
        return this
    }

    fun selectAccount(): SearchRobot {
        onView(withId(R.id.explore_search_input_account_item_view))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
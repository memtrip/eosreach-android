package com.memtrip.eosreach.robot.proxyvoter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder

class ProxyVoterRobot {

    fun verifyProxyVoterListScreen(): ProxyVoterRobot {
        onView(withId(R.id.proxy_voter_list_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_list_recyclerview))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectProxyVoterListFirstAccountRow(): ProxyVoterRobot {
        onView(withId(R.id.proxy_voter_list_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<SimpleAdapterViewHolder<ProxyVoterDetails>>(0, click()))
        return this
    }

    fun verifyProxyVoterListErrorScreen(): ProxyVoterRobot {
        onView(withId(R.id.proxy_voter_list_error_view))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectProxyVoterListRetryButton(): ProxyVoterRobot {
        onView(withId(R.id.view_error_composite_retry))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyProxyVoterErrorScreen(): ProxyVoterRobot {
        onView(withId(R.id.proxy_voter_view_error_view))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectProxyVoterRetryButton(): ProxyVoterRobot {
        onView(withId(R.id.view_error_composite_retry))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyProxyVoterScreen(): ProxyVoterRobot {
        onView(withId(R.id.proxy_voter_view_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_frame_background))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_icon))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_candidate_name_label))
            .check(matches(isDisplayed()))
            .check(matches(withText("EOS PROXY \uD83E\uDD47\uD83E\uDD48\uD83E\uDD49")))
        onView(withId(R.id.proxy_voter_view_slogan_label))
            .check(matches(isDisplayed()))
            .check(matches(withText("\uD83C\uDF0F Delegate your vote with confidence \uD83C\uDF0F")))
        onView(withId(R.id.proxy_voter_view_website_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_owner_account_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_philosophy_container))
            .check(matches(isDisplayed()))
        onView(withId(R.id.proxy_voter_view_philosophy_label))
            .check(matches(isDisplayed()))
            .check(matches(withText("This is going to be the philosophy of the proxy voter")))
        return this
    }
}
package com.memtrip.eosreach.robot.account.vote

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.atPosition

class VoteRobot {

    fun verifyNotVotedScreen(): VoteRobot {
        onView(withId(R.id.vote_no_vote_body_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_no_vote_memtripblock_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_no_vote_castvote_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyVotedBlockProducersScreen(): VoteRobot {
        onView(withId(R.id.vote_producer_vote_title_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_producer_vote_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("memtripblock")))))
            .check(matches(atPosition(1, hasDescendant(withText("eosflareiobp")))))
        return this
    }

    fun verifyProxyVoteScreen(): VoteRobot {
        onView(withId(R.id.vote_proxy_title))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_proxy_voter))
            .check(matches(isDisplayed()))
            .check(matches(withText("memtripproxy")))
        return this
    }

    fun selectVoteForUs(): VoteRobot {
        onView(withId(R.id.vote_no_vote_castvote_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectVoteForProxy(): VoteRobot {
        onView(withId(R.id.vote_cast_vote_proxy_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyCastProxyVoteScreen(): VoteRobot {
        onView(withId(R.id.cast_proxy_vote_name_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cast_proxy_vote_name_input))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cast_proxy_vote_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun typeCastProxyVote(value: String): VoteRobot {
        onView(withId(R.id.cast_proxy_vote_name_input))
            .check(matches(isDisplayed()))
            .perform(typeText(value))
            .perform(closeSoftKeyboard())
        return this
    }

    fun verifyCastProxyVoteInputValue(value: String): VoteRobot {
        onView(withId(R.id.cast_proxy_vote_name_input))
            .check(matches(isDisplayed()))
            .check(matches(withText(value)))
        return this
    }

    fun selectCastProxyVoteButton(): VoteRobot {
        onView(withId(R.id.cast_proxy_vote_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
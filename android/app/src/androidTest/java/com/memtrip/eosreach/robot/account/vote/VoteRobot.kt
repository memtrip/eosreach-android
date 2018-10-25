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
package com.memtrip.eosreach.robot.account.vote

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers

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

    fun verifyVoteReadOnly(): VoteRobot {
        onView(withId(R.id.vote_cast_vote_title))
            .check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.vote_cast_vote_producer_button))
            .check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.vote_cast_vote_proxy_button))
            .check(matches(Matchers.not(isDisplayed())))
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

    fun verifyFirstBlockProducerItem(producerName: String): VoteRobot {
        onView(withId(R.id.vote_producer_vote_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText(producerName)))))
        return this
    }

    fun selectFirstBlockProducerItem(): VoteRobot {
        onView(withId(R.id.vote_producer_vote_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("memtripblock")))))
            .perform(RecyclerViewActions.actionOnItemAtPosition<SimpleAdapterViewHolder<AccountEntity>>(0, click()))
        return this
    }

    fun verifyVotedSingleBlockProducersScreen(): VoteRobot {
        onView(withId(R.id.vote_producer_vote_title_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_producer_vote_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("memtripblock")))))
        return this
    }

    fun verifyProxyVoteScreen(): VoteRobot {
        onView(withId(R.id.vote_proxy_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.vote_proxy_view_proxy_voter_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("memtripproxy")))
        return this
    }

    fun selectProxyVoteItem(): VoteRobot {
        onView(withId(R.id.vote_proxy_view_proxy_voter_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectVoteForUs(): VoteRobot {
        onView(withId(R.id.vote_no_vote_castvote_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectVoteForProducers(): VoteRobot {
        onView(withId(R.id.vote_cast_vote_producer_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectAddBlockProducerFromList(): VoteRobot {
        onView(withId(R.id.cast_producers_vote_blockproducer_form_block_producer_list))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyVoteForProducersScreen(): VoteRobot {
        onView(withId(R.id.cast_producers_vote_blockproducer_instructions_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cast_producers_vote_blockproducer_form_add))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cast_producers_vote_blockproducer_form_scrollview))
            .check(matches(isDisplayed()))
        onView(withId(R.id.cast_producers_vote_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectRemoveProducerButton(): VoteRobot {
        onView(withId(R.id.cast_producers_vote_item_remove))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectVoteForProducersAddButton(): VoteRobot {
        onView(withId(R.id.cast_producers_vote_blockproducer_form_add))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyBlockProducerFormEntryValue(value: String): VoteRobot {
        onView(allOf(
            withId(R.id.cast_producers_vote_blockproducer_name_input),
            isDescendantOfA(withId(R.id.cast_producers_vote_blockproducer_form_container))
        )).check(matches(isDisplayed()))
            .check(matches(withText(value)))
        return this
    }

    fun typeCastProducerVote(value: String): VoteRobot {
        onView(allOf(
            isDescendantOfA(withId(R.id.cast_producers_vote_blockproducer_form_container)),
            withId(R.id.cast_producers_vote_blockproducer_name_input)
        )).check(matches(isDisplayed()))
            .perform(typeText(value))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectCastProducerVoteButton(): VoteRobot {
        onView(withId(R.id.cast_producers_vote_button))
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

    fun selectExploreProxyAccountsButton(): VoteRobot {
        onView(withId(R.id.cast_proxy_explore_proxy_accounts_button))
            .check(matches(isDisplayed()))
            .perform(click())
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
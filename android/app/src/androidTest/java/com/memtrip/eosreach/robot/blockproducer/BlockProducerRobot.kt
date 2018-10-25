package com.memtrip.eosreach.robot.blockproducer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.clickChildViewWithId
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder

class BlockProducerRobot {

    fun verifyBlockProducerListScreen(): BlockProducerRobot {
        onView(withId(R.id.block_producer_list_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.block_producer_list_recyclerview))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyBlockProducerListFirstAccountRow(): BlockProducerRobot {
        onView(withId(R.id.block_producer_list_recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("eosnewyorkio")))))
            .check(matches(atPosition(0, hasDescendant(withText("EOS New York")))))
        return this
    }

    fun selectBlockProducerListFirstAccountRow(): BlockProducerRobot {
        onView(withId(R.id.block_producer_list_recyclerview))
            .perform(actionOnItemAtPosition<SimpleAdapterViewHolder<BlockProducerDetails>>(0, click()))
        return this
    }

    fun selectBlockProducerListFirstAccountRowInformation(): BlockProducerRobot {
        onView(withId(R.id.block_producer_list_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SimpleAdapterViewHolder<BlockProducerDetails>>(
                0, clickChildViewWithId(R.id.block_producer_list_item_information)))
        return this
    }

    fun selectBlockProducerListErrorRetryButton(): BlockProducerRobot {
        onView(withId(R.id.block_producer_list_error_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_error_composite_retry))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyViewBlockProducerOnChainMissingLabel(): BlockProducerRobot {
        onView(withId(R.id.block_producer_view_empty_label))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectViewBlockProducerErrorRetryButton(): BlockProducerRobot {
        onView(withId(R.id.block_producer_view_error))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_error_composite_retry))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyViewBlockProducerInformationScreen(
        candidateName: String,
        website: String,
        email: String
    ): BlockProducerRobot {
        onView(withId(R.id.block_producer_view_icon))
            .check(matches(isDisplayed()))
        onView(withId(R.id.block_producer_view_candidate_name_label))
            .check(matches(isDisplayed()))
            .check(matches(withText(candidateName)))
        onView(withId(R.id.block_producer_view_website_label))
            .check(matches(isDisplayed()))
            .check(matches(withText(website)))
        onView(withId(R.id.block_producer_view_email_label))
            .check(matches(isDisplayed()))
            .check(matches(withText(email)))
        onView(withId(R.id.block_producer_view_owner_account_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.block_producer_view_code_of_conduct_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.block_producer_view_ownership_disclosure_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectViewOwnerAccount(): BlockProducerRobot {
        onView(withId(R.id.block_producer_view_owner_account_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
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
package com.memtrip.eosreach.robot.account.resources

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthBundle
import com.memtrip.eosreach.atPosition
import com.memtrip.eosreach.uikit.SimpleAdapterViewHolder
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf

class BandwidthRobot {

    fun verifyManageBandwidthScreen(): BandwidthRobot {
        Espresso.onView(withId(R.id.manage_bandwidth_toolbar))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.manage_bandwidth_tablayout))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.manage_bandwidth_viewpager))
            .check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun selectUndelegateTab(): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withText(R.string.resources_manage_bandwidth_tab_undelegate),
            isDescendantOfA(withId(R.id.manage_bandwidth_tablayout))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun selectAllocatedTab(): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withText(R.string.resources_manage_bandwidth_tab_allocated),
            isDescendantOfA(withId(R.id.manage_bandwidth_tablayout))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun enterUsername(accountName: String, fragmentId: Int): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withId(R.id.manage_bandwidth_target_account_form_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.typeText(accountName))
            .perform(ViewActions.pressImeActionButton())
        return this
    }

    fun enterNetBalance(amount: String, fragmentId: Int): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withId(R.id.manage_bandwidth_net_amount_form_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(clearText())
            .perform(ViewActions.typeText(amount))
            .perform(ViewActions.pressImeActionButton())
        return this
    }

    fun enterCpuBalance(amount: String, fragmentId: Int): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withId(R.id.manage_bandwidth_cpu_amount_form_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(clearText())
            .perform(ViewActions.typeText(amount))
            .perform(ViewActions.closeSoftKeyboard())
        return this
    }

    fun selectBandwidthFormCtaButton(fragmentId: Int): BandwidthRobot {
        Espresso.onView(CoreMatchers.allOf(
            withId(R.id.manage_bandwidth_form_cta_button),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun verifyBandwidthConfirmNetBalance(amount: String): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_details_net_value))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(withText(amount)))
        return this
    }

    fun verifyBandwidthConfirmCpuBalance(amount: String): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_details_cpu_value))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(withText(amount)))
        return this
    }

    fun verifyBandwidthConfirmScreen(): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_confirm_details_layout))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.bandwidth_confirm_cta_button))
            .check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun selectBandwidthConfirmButton(): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_confirm_cta_button))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun verifyBandwidthConfirmGenericError(): BandwidthRobot {
        Espresso.onView(withText(R.string.app_dialog_generic_error_body))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withText(R.string.app_dialog_positive_button))
            .check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun selectBandwidthConfirmGenericErrorPositiveButton(): BandwidthRobot {
        Espresso.onView(withText(R.string.app_dialog_positive_button))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun verifyAllocatedBandwidthRow(): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_delegate_list_recyclerview))
            .check(ViewAssertions.matches(atPosition(0, hasDescendant(withText("memtripadmin")))))
            .check(ViewAssertions.matches(atPosition(0, hasDescendant(withText("10.0200 SYS")))))
            .check(ViewAssertions.matches(atPosition(0, hasDescendant(withText("50.0200 SYS")))))
        return this
    }

    fun verifyEmptyAllocatedBandwidth(): BandwidthRobot {
        onView(withId(R.id.bandwidth_delegate_list_empty))
            .check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun verifyErrorAllocatedBandwidth(): BandwidthRobot {
        onView(withId(R.id.bandwidth_delegate_list_error))
            .check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun selectErrorRetryAllocatedBandwidth(): BandwidthRobot {
        onView(allOf(
            withId(R.id.view_error_composite_retry),
            isDescendantOfA(withId(R.id.bandwidth_delegate_list_error))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectFirstAllocatedBandwidthListItem(): BandwidthRobot {
        Espresso.onView(withId(R.id.bandwidth_delegate_list_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<SimpleAdapterViewHolder<BandwidthBundle>>(0, ViewActions.click()))
        return this
    }
}
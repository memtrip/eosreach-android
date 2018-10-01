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

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import org.hamcrest.CoreMatchers.allOf

class ResourcesRobot {

    fun verifyResourcesScreen(): ResourcesRobot {
        onView(withId(R.id.resources_manage_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectBandwidthButton(): ResourcesRobot {
        onView(withId(R.id.resources_manage_bandwidth_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectRamButton(): ResourcesRobot {
        onView(withId(R.id.resources_manage_ram_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyManageBandwidthScreen(): ResourcesRobot {
        onView(withId(R.id.manage_bandwidth_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_bandwidth_tablayout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_bandwidth_viewpager))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectUndelegateTab(): ResourcesRobot {
        onView(allOf(
            withText(R.string.resources_manage_bandwidth_tab_undelegate),
            isDescendantOfA(withId(R.id.manage_bandwidth_tablayout))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun enterNetBalance(amount: String, fragmentId: Int): ResourcesRobot {
        onView(allOf(
            withId(R.id.manage_bandwidth_net_amount_form_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(matches(isDisplayed()))
            .perform(typeText(amount))
            .perform(pressImeActionButton())
        return this
    }

    fun enterCpuBalance(amount: String, fragmentId: Int): ResourcesRobot {
        onView(allOf(
            withId(R.id.manage_bandwidth_cpu_amount_form_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(matches(isDisplayed()))
            .perform(typeText(amount))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectDelegateButton(fragmentId: Int): ResourcesRobot {
        onView(allOf(
            withId(R.id.manage_bandwidth_form_cta_button),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyConfirmNetBalance(amount: String): ResourcesRobot {
        onView(withId(R.id.bandwidth_details_net_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(amount)))
        return this
    }

    fun verifyConfirmCpuBalance(amount: String): ResourcesRobot {
        onView(withId(R.id.bandwidth_details_cpu_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(amount)))
        return this
    }

    fun selectConfirmButton(): ResourcesRobot {
        onView(withId(R.id.bandwidth_confirm_cta_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyManageRamScreen(): ResourcesRobot {
        onView(withId(R.id.manage_ram_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_ram_current_price_value))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_ram_current_price_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_ram_tablayout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.manage_ram_viewpager))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectSellTab(): ResourcesRobot {
        onView(allOf(
            withText(R.string.resources_manage_ram_tab_sell),
            isDescendantOfA(withId(R.id.manage_ram_tablayout))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun enterRamAmount(amount: String, fragmentId: Int): ResourcesRobot {
        onView(allOf(
            withId(R.id.manage_ram_form_amount_input),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(matches(isDisplayed()))
            .perform(typeText(amount))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectCtaButton(fragmentId: Int): ResourcesRobot {
        onView(allOf(
            withId(R.id.manage_ram_form_cta_button),
            isDescendantOfA(withId(fragmentId))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyConfirmBuyTitle(): ResourcesRobot {
        onView(withId(R.id.ram_confirm_toolbar))
            .check(matches(isDisplayed()))
        onView(withText(R.string.resources_ram_confirm_form_buy_label))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyConfirmSellTitle(): ResourcesRobot {
        onView(withId(R.id.ram_confirm_toolbar))
            .check(matches(isDisplayed()))
        onView(withText(R.string.resources_ram_confirm_form_sell_label))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyConfirmKb(amount: String): ResourcesRobot {
        onView(withId(R.id.ram_details_kb_value))
            .check(matches(isDisplayed()))
            .check(matches(withText(amount)))
        return this
    }

    fun verifyPrice(): ResourcesRobot {
        onView(withId(R.id.ram_details_cost_value))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectConfirmCta(): ResourcesRobot {
        onView(withId(R.id.ram_confirm_cta_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
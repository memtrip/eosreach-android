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
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.memtrip.eosreach.R
import org.hamcrest.CoreMatchers

class RamRobot {

    fun verifyManageRamScreen(): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.manage_ram_toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.manage_ram_current_price_value))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.manage_ram_current_price_label))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.manage_ram_tablayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.manage_ram_viewpager))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun selectSellTab(): RamRobot {
        Espresso.onView(CoreMatchers.allOf(
            ViewMatchers.withText(R.string.resources_manage_ram_tab_sell),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.manage_ram_tablayout))
        ))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun enterRamAmount(amount: String, fragmentId: Int): RamRobot {
        Espresso.onView(CoreMatchers.allOf(
            ViewMatchers.withId(R.id.manage_ram_form_amount_input),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(fragmentId))
        ))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.typeText(amount))
            .perform(ViewActions.closeSoftKeyboard())
        return this
    }

    fun selectCtaButton(fragmentId: Int): RamRobot {
        Espresso.onView(CoreMatchers.allOf(
            ViewMatchers.withId(R.id.manage_ram_form_cta_button),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(fragmentId))
        ))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun verifyConfirmBuyTitle(): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.ram_confirm_toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.resources_ram_confirm_form_buy_label))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun verifyConfirmSellTitle(): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.ram_confirm_toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.resources_ram_confirm_form_sell_label))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun verifyConfirmKb(amount: String): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.ram_details_kb_value))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(amount)))
        return this
    }

    fun verifyPrice(): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.ram_details_cost_value))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun selectConfirmCta(): RamRobot {
        Espresso.onView(ViewMatchers.withId(R.id.ram_confirm_cta_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this
    }
}
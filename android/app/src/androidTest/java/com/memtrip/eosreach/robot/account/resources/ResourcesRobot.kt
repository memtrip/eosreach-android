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
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.memtrip.eosreach.R
import org.hamcrest.Matchers

class ResourcesRobot {

    fun verifyResourcesScreen(): ResourcesRobot {
        onView(withId(R.id.resources_manage_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyResourcesReadOnly(): ResourcesRobot {
        onView(withId(R.id.resources_manage_title))
            .check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.resources_manage_bandwidth_button))
            .check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.resources_manage_ram_button))
            .check(matches(Matchers.not(isDisplayed())))
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
}
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
package com.memtrip.eosreach.robot

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class CommonRobot {

    fun clickDialogOk(): CommonRobot {
        onView(withText("OK"))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun pressBack(): CommonRobot {
        Espresso.pressBack()
        return this
    }

    fun pressHomeUp(): CommonRobot {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
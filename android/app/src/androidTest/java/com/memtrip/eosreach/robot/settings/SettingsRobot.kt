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
package com.memtrip.eosreach.robot.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class SettingsRobot {

    fun verifySettingsScreen(): SettingsRobot {
        onView(withId(R.id.settings_toolbar))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectViewPrivateKeysSettingsItem(): SettingsRobot {
        onView(withId(R.id.settings_view_private_keys_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyViewPrivateKeysScreen(): SettingsRobot {
        onView(withId(R.id.view_private_keys_toolbar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_private_keys_instructions))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_private_keys_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectShowPrivateKeysButton(): SettingsRobot {
        onView(withId(R.id.view_private_keys_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyNoPrivateKeys(): SettingsRobot {
        onView(withId(R.id.view_private_keys_empty))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyViewPrivateKeyScreenPrivateKeyValue(privateKey: String): SettingsRobot {
        onView(withId(R.id.view_private_keys_item_private))
            .check(matches(isDisplayed()))
            .check(matches(withText(privateKey)))
        return this
    }

    fun verifyViewPrivateKeyScreenPublicKeyValue(publicKey: String): SettingsRobot {
        onView(withId(R.id.view_private_keys_item_public))
            .check(matches(isDisplayed()))
            .check(matches(withText(publicKey)))
        return this
    }

    fun verifyViewPrivateKeyScreenAccountsValue(accounts: String): SettingsRobot {
        onView(withId(R.id.view_private_keys_item_accounts))
            .check(matches(isDisplayed()))
            .check(matches(withText(accounts)))
        return this
    }
}
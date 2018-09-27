package com.memtrip.eosreach.robot.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.memtrip.eosreach.R

class SettingsRobot {

    fun verifySettingsScreen(): SettingsRobot {
        onView(withId(R.id.settings_toolbar))
            .check(matches(isDisplayed()))
        return this
    }
}
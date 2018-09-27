package com.memtrip.eosreach.robot.welcome

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.memtrip.eosreach.R

class SplashRobot {

    fun navigateCreateAccount(): SplashRobot {
        onView(ViewMatchers.withId(R.id.welcome_splash_create_account_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this
    }

    fun navigateImportKey(): SplashRobot {
        onView(ViewMatchers.withId(R.id.welcome_splash_import_private_key_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        return this
    }
}
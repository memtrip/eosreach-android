package com.memtrip.eosreach.robot.issue

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R

class ImportKeyRobot {

    fun verifyImportKeyScreen(): ImportKeyRobot {
        onView(ViewMatchers.withId(R.id.issue_import_key_toolbar))
            .check(matches(isDisplayed()))
        return this
    }

    fun typePrivateKey(privateKey: String): ImportKeyRobot {
        onView(ViewMatchers.withId(R.id.issue_import_key_private_key_value_input))
            .check(matches(isDisplayed()))
            .perform(typeText(privateKey))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectImportButton(): ImportKeyRobot {
        onView(ViewMatchers.withId(R.id.issue_import_key_import_button))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyInvalidKeyError(): ImportKeyRobot {
        onView(withText(R.string.issue_import_key_error_invalid_private_key_format))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyNoAccountsError(): ImportKeyRobot {
        onView(withText(R.string.issue_import_key_error_no_accounts))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyGenericError(): ImportKeyRobot {
        onView(withText(R.string.issue_import_key_error_generic))
            .check(matches(isDisplayed()))
        return this
    }
}
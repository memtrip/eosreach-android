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
package com.memtrip.eosreach.robot.issue

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.memtrip.eosreach.R
import org.hamcrest.Matchers.allOf

class CreateAccountRobot {

    fun verifySkuNotFound(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_sku_error))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_sku_error))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyBillingUnavailable(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_sku_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_billing_setup_error))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyEnterAccountNameScreen(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_name_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_name_input))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_instructions_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_create_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun typeAccountName(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_name_input))
            .check(matches(isDisplayed()))
            .perform(typeText("memtripblock"))
            .perform(closeSoftKeyboard())
        return this
    }

    fun typeAccountNameUnder12Characters(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_name_input))
            .check(matches(isDisplayed()))
            .perform(typeText("memtrip"))
            .perform(closeSoftKeyboard())
        return this
    }

    fun typeAccountNameStartingWithNumber(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_name_input))
            .check(matches(isDisplayed()))
            .perform(typeText("1memtripbloc"))
            .perform(closeSoftKeyboard())
        return this
    }

    fun selectCreateAccountButton(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_create_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyAccountMustBe12CharactersError(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_username_validation_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyAccountMustStartWithLetterError(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_username_number_start_validation_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyBillingFlowCancelled(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_billing_purchase_cancelled_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyBillingFlowError(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_billing_purchase_failed_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyBillingFlowPurchaseIdFatalError(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_billing_purchase_fatal_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyCreateAccountError(): CreateAccountRobot {
        onView(withText(R.string.app_dialog_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_generic_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.app_dialog_positive_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyLimboScreen(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_limbo_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_limbo_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_limbo_body))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectLimboRetryButton(): CreateAccountRobot {
        onView(allOf(
            withId(R.id.view_error_composite_retry),
            isDescendantOfA(withId(R.id.issue_create_account_limbo_error))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectLimboSettingsButton(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_limbo_settings_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyAccountCreatedScreen(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_import_key_instruction_title_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_import_key_instruction_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_import_key_label))
            .check(matches(isDisplayed()))
        onView(withId(R.id.issue_create_account_import_key_done_button))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectAccountCreatedDoneButton(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_import_key_done_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun verifyImportAccountError(): CreateAccountRobot {
        onView(withText(R.string.issue_create_account_import_key_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_import_key_error_body))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyCouldNotRetrieveAccountError(): CreateAccountRobot {
        onView(withText(R.string.issue_create_account_import_key_error_title))
            .check(matches(isDisplayed()))
        onView(withText(R.string.issue_create_account_import_key_no_accounts))
            .check(matches(isDisplayed()))
        return this
    }

    fun selectImportKeySettingsButton(): CreateAccountRobot {
        onView(withId(R.id.issue_create_account_import_key_settings_button))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }

    fun selectImportKeyRetryButton(): CreateAccountRobot {
        onView(allOf(
            withId(R.id.view_error_composite_retry),
            isDescendantOfA(withId(R.id.issue_create_account_import_key_error))
        ))
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
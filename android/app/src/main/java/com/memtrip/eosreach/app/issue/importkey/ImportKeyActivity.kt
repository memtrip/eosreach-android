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
package com.memtrip.eosreach.app.issue.importkey

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_import_key_activity.*
import javax.inject.Inject

abstract class ImportKeyActivity
    : MviActivity<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState, ImportKeyViewLayout>(), ImportKeyViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ImportKeyViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_import_key_activity)

        setSupportActionBar(issue_import_key_toolbar)
        supportActionBar!!.title = getString(R.string.issue_import_key_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.welcome_menu, menu)

        menu.findItem(R.id.welcome_menu_settings).setOnMenuItemClickListener {
            model().publish(ImportKeyIntent.NavigateToSettings)
            true
        }

        return true
    }

    override fun intents(): Observable<ImportKeyIntent> = Observable.merge(
        Observable.merge(
            RxView.clicks(issue_import_key_import_button),
            RxTextView.editorActionEvents(issue_import_key_private_key_value_input)
        ).map {
            ImportKeyIntent.ImportKey(issue_import_key_private_key_value_input.text.toString())
        },
        RxView.clicks(issue_import_key_github_button).map {
            ImportKeyIntent.ViewSource
        }
    )

    override fun layout(): ImportKeyViewLayout = this

    override fun model(): ImportKeyViewModel = getViewModel(viewModelFactory)

    override fun render(): ImportKeyViewRenderer = render

    override fun showDefaults() {
        if (showGithubViewSource()) {
            issue_import_key_instructions_label.visible()
            issue_import_key_github_button.visible()
        } else {
            issue_import_key_instructions_label.gone()
            issue_import_key_github_button.gone()
        }
    }

    override fun showProgress() {
        issue_import_key_progressbar.visible()
        issue_import_key_import_button.invisible()
        hideKeyboard()
    }

    override fun showError(error: String) {
        issue_import_key_progressbar.gone()
        issue_import_key_import_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(error)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun navigateToGithubSource() {
        model().publish(ImportKeyIntent.Init)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.issue_import_key_github_source_url)
        )))
    }

    override fun navigateToSettings() {
        model().publish(ImportKeyIntent.Init)
        startActivity(settingsIntent(this))
    }

    abstract fun showGithubViewSource(): Boolean

    abstract override fun inject()

    abstract override fun success()
}

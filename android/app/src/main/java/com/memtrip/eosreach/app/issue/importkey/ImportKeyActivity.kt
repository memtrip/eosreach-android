package com.memtrip.eosreach.app.issue.importkey

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
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
    }

    override fun intents(): Observable<ImportKeyIntent> = Observable.merge(
        RxView.clicks(issue_import_key_import_button).map {
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
    }

    override fun showError(error: String) {
        issue_import_key_progressbar.gone()
        issue_import_key_import_button.visible()
        issue_import_key_private_key_value_label.error = error
    }

    override fun navigateToGithubSource() {
        model().publish(ImportKeyIntent.Init)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.issue_import_key_github_source_url)
        )))
    }

    abstract fun showGithubViewSource(): Boolean

    abstract override fun inject()

    abstract override fun success()
}

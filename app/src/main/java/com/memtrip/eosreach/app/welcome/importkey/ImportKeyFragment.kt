package com.memtrip.eosreach.app.welcome.importkey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

internal class ImportKeyFragment
    : MviFragment<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState, ImportKeyViewLayout>(), ImportKeyViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ImportKeyViewModel>

    @Inject
    lateinit var render: ImportKeyViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_import_key_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<ImportKeyIntent> = Observable.empty()

    override fun layout(): ImportKeyViewLayout = this

    override fun model(): ImportKeyViewModel = getViewModel(viewModelFactory)

    override fun render(): ImportKeyViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}

package com.memtrip.eosreach.app.welcome.keyimported

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_key_imported_fragment.*
import javax.inject.Inject

internal class KeyImportedFragment
    : MviFragment<KeyImportedIntent, KeyImportedRenderAction, KeyImportedViewState, KeyImportedViewLayout>(), KeyImportedViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: KeyImportedViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_key_imported_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<KeyImportedIntent> {
        return RxView.clicks(welcome_key_imported_done_button).map { KeyImportedIntent.Done }
    }

    override fun layout(): KeyImportedViewLayout = this

    override fun model(): KeyImportedViewModel = getViewModel(viewModelFactory)

    override fun render(): KeyImportedViewRenderer = render

    override fun done() {
        NavHostFragment.findNavController(this).navigate(
            R.id.welcome_navigation_accounts_navigation,
            null,
            NavOptions.Builder().build())
    }
}

package com.memtrip.eosreach.app.welcome.newaccount

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

internal class NewAccountFragment
    : MviFragment<NewAccountIntent, NewAccountRenderAction, NewAccountViewState, NewAccountViewLayout>(), NewAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<NewAccountViewModel>

    @Inject
    lateinit var render: NewAccountViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome_new_account_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<NewAccountIntent> = Observable.empty()

    override fun layout(): NewAccountViewLayout = this

    override fun model(): NewAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): NewAccountViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}

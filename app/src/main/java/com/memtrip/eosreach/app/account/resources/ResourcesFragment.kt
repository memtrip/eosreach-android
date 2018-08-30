package com.memtrip.eosreach.app.account.resources

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

class ResourcesFragment
    : MviFragment<ResourcesIntent, ResourcesRenderAction, ResourcesViewState, ResourcesViewLayout>(), ResourcesViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ResourcesViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_resources_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<ResourcesIntent> = Observable.empty()

    override fun layout(): ResourcesViewLayout = this

    override fun model(): ResourcesViewModel = getViewModel(viewModelFactory)

    override fun render(): ResourcesViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
    }

    companion object {
        fun newInstance(): ResourcesFragment {
            return ResourcesFragment()
        }
    }
}

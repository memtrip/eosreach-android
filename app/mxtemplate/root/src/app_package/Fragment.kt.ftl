package ${packageName}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.ViewModelFactory

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.${layoutName}.*

internal class ${className}Fragment
    : MviFragment<${className}Intent, ${className}RenderAction, ${className}ViewState, ${className}ViewLayout>(), ${className}ViewLayout {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var render: ${className}ViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.${layoutName}, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<${className}Intent> = Observable.empty()

    override fun layout(): ${className}ViewLayout = this

    override fun model(): ${className}ViewModel = getViewModel(viewModelFactory)

    override fun render(): ${className}ViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

    }
}

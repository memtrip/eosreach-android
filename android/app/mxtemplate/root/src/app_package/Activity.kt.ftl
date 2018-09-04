package ${packageName}

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.ViewModelFactory

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.${layoutName}.*

class ${className}Activity
    : MviActivity<${className}Intent, ${className}RenderAction, ${className}ViewState, ${className}ViewLayout>(), ${className}ViewLayout {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var render: ${className}ViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.${layoutName})
    }

    override fun inject() {
        AndroidInjection.inject(this)
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

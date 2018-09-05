package com.memtrip.eosreach.app.settings.viewprivatekeys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater

import android.view.WindowManager
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.ViewModelFactory

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.view_private_keys_activity.*

class ViewPrivateKeysActivity
    : MviActivity<ViewPrivateKeysIntent, ViewPrivateKeysRenderAction, ViewPrivateKeysViewState, ViewPrivateKeysViewLayout>(), ViewPrivateKeysViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewPrivateKeysViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_private_keys_activity)
        setSupportActionBar(view_private_keys_toolbar)
        supportActionBar!!.title = getString(R.string.view_private_keys_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewPrivateKeysIntent>  {
        return RxView.clicks(view_private_keys_button).map {
            ViewPrivateKeysIntent.DecryptPrivateKeys
        }
    }

    override fun layout(): ViewPrivateKeysViewLayout = this

    override fun model(): ViewPrivateKeysViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewPrivateKeysViewRenderer = render

    override fun showPrivateKeys(privateKeys: List<EosPrivateKey>) {
        view_private_keys_button.gone()
        view_private_keys_progressbar.gone()
        view_private_keys_data_scrollview.visible()
        privateKeys.map {
            view_private_keys_data_container.addView(
                with (LayoutInflater.from(this).inflate(
                    R.layout.view_private_keys_item_layout,
                    null,
                    false
                ) as TextView) {
                    text = privateKeys.toString()
                    this
                }
            )
        }
    }

    override fun showProgress() {
        view_private_keys_instructions.gone()
        view_private_keys_button.gone()
        view_private_keys_progressbar.visible()
    }

    companion object {

        fun viewPrivateKeysIntent(context: Context): Intent {
            return Intent(context, ViewPrivateKeysActivity::class.java)
        }
    }
}

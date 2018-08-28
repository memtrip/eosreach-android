package com.memtrip.eosreach.app

import androidx.fragment.app.Fragment

import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector

import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class EosReachApplication : DaggerApplication(), HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun applicationInjector(): AndroidInjector<EosReachApplication> = daggerEosReachApplicationComponent

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) { return }

        LeakCanary.install(this)

        super.onCreate()
    }

    private val daggerEosReachApplicationComponent by lazy {
        DaggerEosReachApplicationComponent
            .builder()
            .application(this)
            .build()
    }
}
package com.memtrip.eosreach.app

import android.app.Activity
import androidx.fragment.app.Fragment
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseEosReachApplication : DaggerApplication(), HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun applicationInjector(): AndroidInjector<EosReachApplication> = daggerEosReachApplicationComponent

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) { return }

        LeakCanary.install(this)

        super.onCreate()
    }

    abstract val daggerEosReachApplicationComponent: EosReachApplicationComponent
}
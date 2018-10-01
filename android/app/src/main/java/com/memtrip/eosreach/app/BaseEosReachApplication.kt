/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app

import android.app.Activity
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.threetenabp.AndroidThreeTen

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

    override fun applicationInjector(): AndroidInjector<BaseEosReachApplication> = daggerEosReachApplicationComponent

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) { return }

        LeakCanary.install(this)

        Fresco.initialize(this)

        AndroidThreeTen.init(this)

        super.onCreate()
    }

    abstract val daggerEosReachApplicationComponent: EosReachApplicationComponent
}
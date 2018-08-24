package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.app.welcome.WelcomeNavigationActivityModule

import dagger.BindsInstance
import dagger.Component

import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    WelcomeNavigationActivityModule::class
])
interface EosReachApplicationComponent : AndroidInjector<EosReachApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): EosReachApplicationComponent
    }
}
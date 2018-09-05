package com.memtrip.eosreach.app.settings

import com.memtrip.eosreach.app.settings.eosendpoint.EosEndpointActivity
import com.memtrip.eosreach.app.settings.eosendpoint.EosEndpointActivityModule
import com.memtrip.eosreach.app.settings.viewprivatekeys.ViewPrivateKeysActivity
import com.memtrip.eosreach.app.settings.viewprivatekeys.ViewPrivateKeysActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsModule {

    @ContributesAndroidInjector(modules = [SettingsActivityModule::class])
    internal abstract fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [EosEndpointActivityModule::class])
    internal abstract fun contributeEosEndpointActivity(): EosEndpointActivity

    @ContributesAndroidInjector(modules = [ViewPrivateKeysActivityModule::class])
    internal abstract fun contributeViewPrivateKeysActivity(): ViewPrivateKeysActivity
}
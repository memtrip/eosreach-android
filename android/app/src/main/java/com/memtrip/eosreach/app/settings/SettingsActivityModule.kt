package com.memtrip.eosreach.app.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSettingsActivity(): SettingsActivity
}
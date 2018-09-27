package com.memtrip.eosreach

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.memtrip.eosreach.app.welcome.EntryActivity

abstract class BaseActivityTestRule : ActivityTestRule<EntryActivity>(EntryActivity::class.java, true, false) {

    fun launch() {
        launchActivity(null)
    }

    abstract fun inject()

    abstract fun resetInjector()

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()

        if (animationsEnabled(InstrumentationRegistry.getTargetContext())) {
            throw IllegalStateException("\n\n *** \n *** DEVICE NOT CONFIGURED \n *** \n\n" +
                "Please ensure the following animation settings are disabled in your device Developer options: \n" +
                "- window_animation_scale \n" +
                "- transition_animation_scale \n" +
                "- animator_duration_scale \n" +
                "More detail: " +
                "https://google.github.io/android-testing-support-library/docs/espresso/setup/index.html#setup-your-test-environment \n")
        }

        clearAppData()

        inject()
    }

    override fun afterActivityFinished() {
        super.afterActivityFinished()
        resetInjector()
    }

    private fun animationsEnabled(context: Context): Boolean {

        var animationsEnabled = 0f

        animationsEnabled += Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE, 1f)

        animationsEnabled += Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.TRANSITION_ANIMATION_SCALE, 1f)

        animationsEnabled += Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.WINDOW_ANIMATION_SCALE, 1f)

        return animationsEnabled != 0f
    }

    private fun clearAppData() {
        getSharedPreferences("com.memtrip.eosreach").edit().clear().commit()
        getSharedPreferences("com.memtrip.eosreach.keys").edit().clear().commit()
        InstrumentationRegistry.getTargetContext().deleteDatabase("eosreach")
    }

    private fun getSharedPreferences(key: String): SharedPreferences {
        return InstrumentationRegistry.getTargetContext().getSharedPreferences(key, Context.MODE_PRIVATE)
    }
}
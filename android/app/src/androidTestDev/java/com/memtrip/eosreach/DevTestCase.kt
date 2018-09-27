package com.memtrip.eosreach

import androidx.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class DevTestCase : TestCase() {

    @Test
    fun run() {

        rule.launch()

        test()
    }

    abstract fun test()
}
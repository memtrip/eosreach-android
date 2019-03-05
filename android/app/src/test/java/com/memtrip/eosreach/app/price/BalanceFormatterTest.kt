package com.memtrip.eosreach.app.price

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(JUnitPlatform::class)
class BalanceFormatterTest : Spek({

    given("a BalanceFormatter") {

        on("format balance digits") {
            assertEquals("1.2770", BalanceFormatter.formatBalanceDigits(1.277))
            assertEquals("100.2774", BalanceFormatter.formatBalanceDigits(100.27732323))
            assertEquals("1000001.0000", BalanceFormatter.formatBalanceDigits(1000000.99999))
            assertEquals("9.5900", BalanceFormatter.formatBalanceDigits(9.59))
        }

        on("format balance with Cyrillic locale") {
            Locale.setDefault(Locale("ru"))
            assertEquals("1.2770", BalanceFormatter.formatBalanceDigits(1.277))
            assertEquals("100.2774", BalanceFormatter.formatBalanceDigits(100.27732323))
            assertEquals("1000001.0000", BalanceFormatter.formatBalanceDigits(1000000.99999))
            assertEquals("9.5900", BalanceFormatter.formatBalanceDigits(9.59))
        }
    }
})
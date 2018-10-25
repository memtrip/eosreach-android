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
package com.memtrip.eosreach.app.blockproducer

import android.content.res.Resources
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.uikit.gone
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.block_producer_view_activity.*

class ReadOnlyViewBlockProducerActivity : ViewBlockProducerActivity() {

    override fun populate(blockProducerDetails: BlockProducerDetails) {
        super.populate(blockProducerDetails)
        block_producer_view_owner_account_button.gone()
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.ReadOnlyAppTheme, true)
        return theme
    }
}
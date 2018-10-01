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
package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivity
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivityModule
import com.memtrip.eosreach.app.transfer.form.TransferFormActivity
import com.memtrip.eosreach.app.transfer.form.TransferFormActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TransferModule {

    @ContributesAndroidInjector(modules = [TransferFormActivityModule::class])
    internal abstract fun contributeTransferActivity(): TransferFormActivity

    @ContributesAndroidInjector(modules = [TransferConfirmActivityModule::class])
    internal abstract fun contributeTransferConfirmActivity(): TransferConfirmActivity
}
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
package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CastModule {

    @ContributesAndroidInjector(modules = [CastVoteActivityModule::class])
    internal abstract fun contributeCastVoteActivity(): CastVoteActivity

    @ContributesAndroidInjector(modules = [CastProxyVoteFragmentModule::class])
    internal abstract fun contributeCastProxyVoteFragment(): CastProxyVoteFragment

    @ContributesAndroidInjector(modules = [CastProducersVoteFragmentModule::class])
    internal abstract fun contributeCastProducersVoteFragment(): CastProducersVoteFragment
}
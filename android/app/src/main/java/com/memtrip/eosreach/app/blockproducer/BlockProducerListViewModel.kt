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

import android.app.Application
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class BlockProducerListViewModel @Inject internal constructor(
    private val blockProducerRequest: BlockProducerRequest,
    application: Application
) : MxViewModel<BlockProducerListIntent, BlockProducerListRenderAction, BlockProducerListViewState>(
    BlockProducerListViewState(view = BlockProducerListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BlockProducerListIntent): Observable<BlockProducerListRenderAction> = when (intent) {
        BlockProducerListIntent.Idle -> Observable.just(BlockProducerListRenderAction.Idle)
        BlockProducerListIntent.Init -> getBlockProducerList()
        BlockProducerListIntent.Retry -> getBlockProducerList()
        is BlockProducerListIntent.BlockProducerSelected -> Observable.just(BlockProducerListRenderAction.BlockProducerSelected(intent.blockProducerDetails))
        is BlockProducerListIntent.BlockProducerInformationSelected -> Observable.just(BlockProducerListRenderAction.BlockProducerInformationSelected(intent.blockProducerDetails))
    }

    override fun reducer(previousState: BlockProducerListViewState, renderAction: BlockProducerListRenderAction): BlockProducerListViewState = when (renderAction) {
        BlockProducerListRenderAction.Idle -> previousState.copy(
            view = BlockProducerListViewState.View.Idle)
        BlockProducerListRenderAction.OnProgress -> previousState.copy(
            view = BlockProducerListViewState.View.OnProgress)
        BlockProducerListRenderAction.OnError -> previousState.copy(
            view = BlockProducerListViewState.View.OnError)
        is BlockProducerListRenderAction.OnSuccess -> previousState.copy(
            view = BlockProducerListViewState.View.OnSuccess(renderAction.blockProducerList))
        is BlockProducerListRenderAction.BlockProducerSelected -> previousState.copy(
            view = BlockProducerListViewState.View.BlockProducerSelected(renderAction.blockProducer))
        is BlockProducerListRenderAction.BlockProducerInformationSelected -> previousState.copy(
            view = BlockProducerListViewState.View.BlockProducerInformationSelected(renderAction.blockProducerDetails))
    }

    override fun filterIntents(intents: Observable<BlockProducerListIntent>): Observable<BlockProducerListIntent> = Observable.merge(
        intents.ofType(BlockProducerListIntent.Init::class.java).take(1),
        intents.filter {
            !BlockProducerListIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getBlockProducerList(): Observable<BlockProducerListRenderAction> {
        return blockProducerRequest.getBlockProducers(50)
            .toObservable()
            .flatMap<BlockProducerListRenderAction> { response ->
                if (response.success) {
                    Observable.just(BlockProducerListRenderAction.OnSuccess(response.data!!))
                } else {
                    Observable.just(BlockProducerListRenderAction.OnError)
                }
            }
            .onErrorReturn {
                BlockProducerListRenderAction.OnError
            }
            .startWith(BlockProducerListRenderAction.OnProgress)
    }
}
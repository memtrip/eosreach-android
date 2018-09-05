package com.memtrip.eosreach.app.blockproducerlist

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class BlockProducerListViewModel @Inject internal constructor(
    private val blockProducerListUseCase: BlockProducerListUseCase,
    application: Application
) : MxViewModel<BlockProducerListIntent, BlockProducerListRenderAction, BlockProducerListViewState>(
    BlockProducerListViewState(view = BlockProducerListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BlockProducerListIntent): Observable<BlockProducerListRenderAction> = when (intent) {
        BlockProducerListIntent.Init -> getBlockProducerList()
        BlockProducerListIntent.Retry -> getBlockProducerList()
        is BlockProducerListIntent.BlockProducerSelected -> Observable.just(BlockProducerListRenderAction.BlockProducerSelected(intent.blockProducerEntity))
    }

    override fun reducer(previousState: BlockProducerListViewState, renderAction: BlockProducerListRenderAction): BlockProducerListViewState = when (renderAction) {
        BlockProducerListRenderAction.OnProgress -> previousState.copy(
            view = BlockProducerListViewState.View.OnProgress)
        BlockProducerListRenderAction.OnError -> previousState.copy(
            view = BlockProducerListViewState.View.OnError)
        is BlockProducerListRenderAction.OnSuccess -> previousState.copy(
            view = BlockProducerListViewState.View.OnSuccess(renderAction.blockProducerList))
        is BlockProducerListRenderAction.BlockProducerSelected -> previousState.copy(
            view = BlockProducerListViewState.View.BlockProducerSelected(renderAction.blockProducer)
        )
    }

    override fun filterIntents(intents: Observable<BlockProducerListIntent>): Observable<BlockProducerListIntent> = Observable.merge(
        intents.ofType(BlockProducerListIntent.Init::class.java).take(1),
        intents.filter {
            !BlockProducerListIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getBlockProducerList(): Observable<BlockProducerListRenderAction> {
        return blockProducerListUseCase
            .getBlockProducers()
            .toObservable()
            .flatMap<BlockProducerListRenderAction> { blockProducerEntities ->
                if (blockProducerEntities.isNotEmpty()) {
                    Observable.just(BlockProducerListRenderAction.OnSuccess(blockProducerEntities))
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
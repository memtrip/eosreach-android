package com.memtrip.eosreach.app.settings

import android.app.Application
import com.memtrip.eosreach.db.PurgePreferences
import com.memtrip.eosreach.db.account.DeleteAccounts
import com.memtrip.eosreach.db.airdrop.DeleteBalances
import com.memtrip.eosreach.db.blockproducer.DeleteBlockProducers
import com.memtrip.eosreach.db.sharedpreferences.EosPriceCurrencyPair
import com.memtrip.eosreach.db.transaction.DeleteTransactionLog
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SettingsViewModel @Inject internal constructor(
    private val deleteAccounts: DeleteAccounts,
    private val deleteTransactionLog: DeleteTransactionLog,
    private val deleteBalances: DeleteBalances,
    private val deleteBlockProducers: DeleteBlockProducers,
    private val eosKeyManager: EosKeyManager,
    private val purgePreferences: PurgePreferences,
    private val eosPriceCurrencyPair: EosPriceCurrencyPair,
    application: Application
) : MxViewModel<SettingsIntent, SettingsRenderAction, SettingsViewState>(
    SettingsViewState(view = SettingsViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: SettingsIntent): Observable<SettingsRenderAction> = when (intent) {
        SettingsIntent.Idle -> Observable.just(SettingsRenderAction.Idle)
        is SettingsIntent.Init -> getExchangeRateCurrency()
        SettingsIntent.NavigateToCurrencyPairing -> Observable.just(SettingsRenderAction.NavigateToCurrencyPairing)
        SettingsIntent.NavigateToEosEndpoint -> Observable.just(SettingsRenderAction.NavigateToEosEndpoint)
        SettingsIntent.NavigateToPrivateKeys -> Observable.just(SettingsRenderAction.NavigateToPrivateKeys)
        SettingsIntent.NavigateToViewConfirmedTransactions -> Observable.just(SettingsRenderAction.NavigateToViewConfirmedTransactions)
        SettingsIntent.NavigateToTelegram -> Observable.just(SettingsRenderAction.NavigateToTelegram)
        SettingsIntent.RequestClearDataAndLogout -> Observable.just(SettingsRenderAction.ConfirmClearData)
        SettingsIntent.ConfirmClearDataAndLogout -> purgeAll()
        SettingsIntent.NavigateToAuthor -> Observable.just(SettingsRenderAction.NavigateToAuthor)
    }

    override fun reducer(previousState: SettingsViewState, renderAction: SettingsRenderAction): SettingsViewState = when (renderAction) {
        SettingsRenderAction.Idle -> previousState.copy(
            view = SettingsViewState.View.Idle)
        is SettingsRenderAction.Populate -> previousState.copy(
            view = SettingsViewState.View.Populate(renderAction.exchangeRateCurrency))
        SettingsRenderAction.NavigateToCurrencyPairing -> previousState.copy(
            view = SettingsViewState.View.NavigateToCurrencyPairing)
        SettingsRenderAction.NavigateToEosEndpoint -> previousState.copy(
            view = SettingsViewState.View.NavigateToEosEndpoint)
        SettingsRenderAction.NavigateToPrivateKeys -> previousState.copy(
            view = SettingsViewState.View.NavigateToPrivateKeys)
        SettingsRenderAction.NavigateToViewConfirmedTransactions -> previousState.copy(
            view = SettingsViewState.View.NavigateToViewConfirmedTransactions)
        SettingsRenderAction.NavigateToTelegram -> previousState.copy(
            view = SettingsViewState.View.NavigateToTelegram)
        SettingsRenderAction.ConfirmClearData -> previousState.copy(
            view = SettingsViewState.View.ConfirmClearData)
        SettingsRenderAction.NavigateToEntry -> previousState.copy(
            view = SettingsViewState.View.NavigateToEntry)
        SettingsRenderAction.NavigateToAuthor -> previousState.copy(
            view = SettingsViewState.View.NavigateToAuthor)
    }

    override fun filterIntents(intents: Observable<SettingsIntent>): Observable<SettingsIntent> = Observable.merge(
        intents.ofType(SettingsIntent.Init::class.java).take(1),
        intents.filter {
            !SettingsIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getExchangeRateCurrency(): Observable<SettingsRenderAction> {
        return Observable.just(SettingsRenderAction.Populate(eosPriceCurrencyPair.get()))
    }

    private fun purgeAll(): Observable<SettingsRenderAction> {
        return deleteAccounts.remove()
            .doOnComplete {
                deleteTransactionLog.remove()
            }
            .doOnComplete {
                deleteBalances.remove()
            }
            .doOnComplete {
                deleteBlockProducers.remove()
            }
            .doOnComplete {
                eosKeyManager.removeKeystoreEntries()
            }
            .doOnComplete {
                purgePreferences.purgeAll()
            }
            .toSingleDefault<SettingsRenderAction>(SettingsRenderAction.NavigateToEntry)
            .toObservable()
    }
}
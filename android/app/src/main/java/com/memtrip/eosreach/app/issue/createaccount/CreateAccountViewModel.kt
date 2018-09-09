package com.memtrip.eosreach.app.issue.createaccount

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForKeyError
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountError
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single

abstract class CreateAccountViewModel(
    private val keyManager: EosKeyManager,
    private val eosCreateAccountRequest: EosCreateAccountRequest,
    private val accountForPublicKeyRequest: AccountForPublicKeyRequest,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    application: Application
) : MxViewModel<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState>(
    CreateAccountViewState(view = CreateAccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CreateAccountIntent): Observable<CreateAccountRenderAction> = when (intent) {
        is CreateAccountIntent.Init -> Observable.just(CreateAccountRenderAction.Idle)
        is CreateAccountIntent.CreateAccount -> createAccount(intent.purchaseId, intent.accountName)
        CreateAccountIntent.Done -> Observable.just(CreateAccountRenderAction.Done)
    }

    override fun reducer(previousState: CreateAccountViewState, renderAction: CreateAccountRenderAction): CreateAccountViewState = when (renderAction) {
        CreateAccountRenderAction.Idle -> previousState.copy(
            view = CreateAccountViewState.View.Idle)
        CreateAccountRenderAction.OnProgress -> previousState.copy(
            view = CreateAccountViewState.View.OnProgress)
        is CreateAccountRenderAction.OnSuccess -> previousState.copy(
            view = CreateAccountViewState.View.OnSuccess(renderAction.privateKey))
        is CreateAccountRenderAction.OnError -> previousState.copy(
            view = CreateAccountViewState.View.OnError(renderAction.error))
        CreateAccountRenderAction.Done -> previousState.copy(
            view = CreateAccountViewState.View.Done)
    }

    override fun filterIntents(intents: Observable<CreateAccountIntent>): Observable<CreateAccountIntent> = Observable.merge(
        intents.ofType(CreateAccountIntent.Init::class.java).take(1),
        intents.filter {
            !CreateAccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun createAccount(
        purchaseId: String,
        accountName: String
    ) : Observable<CreateAccountRenderAction> {
        return keyManager.importPrivateKey(EosPrivateKey()).flatMap { privateKey ->
            eosCreateAccountRequest.createAccount(
                purchaseId,
                accountName,
                privateKey
            ).map<CreateAccountRenderAction> { result ->
                if (result.success) {
                    CreateAccountRenderAction.OnSuccess(privateKey)
                } else {
                    createAccountError(result.apiError!!)
                }
            }
        }.toObservable()
    }

    private fun createAccountError(error: EosCreateAccountError): CreateAccountRenderAction = when(error) {
        EosCreateAccountError.GenericError -> {
            CreateAccountRenderAction.OnError(
                context().getString(R.string.issue_create_account_generic_error))
        }
        EosCreateAccountError.FatalError -> {
            CreateAccountRenderAction.OnError(
                context().getString(R.string.issue_create_account_fatal_error))
        }
        EosCreateAccountError.AccountNameExists -> {
            CreateAccountRenderAction.OnError(
                context().getString(R.string.issue_create_account_account_name_exists))
        }
    }

    private fun getAccountsForKey(privateKey: String): CreateAccountRenderAction {
        val publicKey = EosPrivateKey(privateKey).publicKey
        return accountForPublicKeyRequest.getAccountsForKey(publicKey).flatMap { result ->
            if (result.success) {
                if (result.data!!.accounts.isEmpty()) {
                    CreateAccountRenderAction.OnError
                } else {
                    CreateAccountRenderAction.OnSuccess()
                }
            } else {
                CreateAccountRenderAction.OnError
            }
        }
    }
}
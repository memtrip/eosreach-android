package com.memtrip.eosreach.app.issue.createaccount

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountError
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.billing.BillingRequest
import com.memtrip.eosreach.db.sharedpreferences.SelectedAccount
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.db.sharedpreferences.UnusedBillingPurchaseId
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single

abstract class CreateAccountViewModel(
    private val keyManager: EosKeyManager,
    private val eosCreateAccountRequest: EosCreateAccountRequest,
    private val accountForPublicKeyRequest: AccountForPublicKeyRequest,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val selectedAccount: SelectedAccount,
    private val unusedBillingPurchaseId: UnusedBillingPurchaseId,
    application: Application
) : MxViewModel<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState>(
    CreateAccountViewState(view = CreateAccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CreateAccountIntent): Observable<CreateAccountRenderAction> = when (intent) {
        is CreateAccountIntent.Init -> getAvailableGooglePlaySku(intent.billingRequest)
        is CreateAccountIntent.SetupGooglePlayBilling -> getAvailableGooglePlaySku(intent.billingRequest)
        is CreateAccountIntent.BuyAccount -> Observable.just(validateAccountName(intent.accountName))
        is CreateAccountIntent.CreateAccount -> createAccount(intent.purchaseToken, intent.accountName)
        is CreateAccountIntent.Done -> getAccountsForKey(intent.privateKey)
    }

    override fun reducer(previousState: CreateAccountViewState, renderAction: CreateAccountRenderAction): CreateAccountViewState = when (renderAction) {
        CreateAccountRenderAction.Idle -> previousState.copy(
            view = CreateAccountViewState.View.Idle)
        CreateAccountRenderAction.OnSkuProgress -> previousState.copy(
            view = CreateAccountViewState.View.OnSkuProgress)
        is CreateAccountRenderAction.OnSkuSuccess -> previousState.copy(
            view = CreateAccountViewState.View.OnSkuSuccess(renderAction.skuDetails))
        is CreateAccountRenderAction.OnGetSkuError -> previousState.copy(
            view = CreateAccountViewState.View.OnGetSkuError(renderAction.message))
        CreateAccountRenderAction.OnAccountNameValidationPassed -> previousState.copy(
            view = CreateAccountViewState.View.OnAccountNameValidationPassed())
        CreateAccountRenderAction.OnCreateAccountProgress -> previousState.copy(
            view = CreateAccountViewState.View.OnCreateAccountProgress)
        is CreateAccountRenderAction.OnCreateAccountSuccess -> previousState.copy(
            view = CreateAccountViewState.View.OnCreateAccountSuccess(renderAction.privateKey))
        is CreateAccountRenderAction.OnCreateAccountError -> previousState.copy(
            view = CreateAccountViewState.View.CreateAccountError(renderAction.error))
        is CreateAccountRenderAction.OnImportKeyError -> previousState.copy(
            view = CreateAccountViewState.View.ImportKeyError(renderAction.error))
        CreateAccountRenderAction.NavigateToAccountList -> previousState.copy(
            view = CreateAccountViewState.View.NavigateToAccountList)
        CreateAccountRenderAction.OnImportKeyProgress -> previousState.copy(
            view = CreateAccountViewState.View.OnImportKeyProgress)
    }

    override fun filterIntents(intents: Observable<CreateAccountIntent>): Observable<CreateAccountIntent> = Observable.merge(
        intents.ofType(CreateAccountIntent.Init::class.java).take(1),
        intents.filter {
            !CreateAccountIntent.Init::class.java.isInstance(it)
        }
    )

    private fun validateAccountName(accountName: String): CreateAccountRenderAction {
        return if (accountName.isEmpty() || accountName.length != 12) {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_username_validation_error))
        } else {
            CreateAccountRenderAction.OnAccountNameValidationPassed
        }
    }

    private fun createAccount(
        purchaseToken: String,
        accountName: String
    ) : Observable<CreateAccountRenderAction> {
        return keyManager.importPrivateKey(EosPrivateKey()).flatMap { privateKey ->
            eosCreateAccountRequest.createAccount(
                purchaseToken,
                accountName,
                privateKey
            ).map<CreateAccountRenderAction> { result ->
                if (result.success) {
                    unusedBillingPurchaseId.clear()
                    CreateAccountRenderAction.OnCreateAccountSuccess(privateKey)
                } else {
                    createAccountError(result.apiError!!)
                }
            }.onErrorReturn {
                CreateAccountRenderAction.OnCreateAccountError(
                    context().getString(R.string.issue_create_account_generic_error))
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnCreateAccountProgress)
    }

    private fun createAccountError(error: EosCreateAccountError): CreateAccountRenderAction = when(error) {
        EosCreateAccountError.GenericError -> {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_generic_error))
        }
        EosCreateAccountError.FatalError -> {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_fatal_error))
        }
        EosCreateAccountError.AccountNameExists -> {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_account_name_exists))
        }
    }

    private fun getAccountsForKey(privateKey: String): Observable<CreateAccountRenderAction> {
        val publicKey = EosPrivateKey(privateKey).publicKey.toString()
        return accountForPublicKeyRequest.getAccountsForKey(publicKey).flatMap { result ->
            if (result.success) {
                if (result.data!!.accounts.isEmpty()) {
                    Single.just(CreateAccountRenderAction.OnCreateAccountError(
                        context().getString(R.string.issue_create_account_import_key_no_accounts)))
                } else {
                    insertAccountsForPublicKey.replace(
                        result.data.publicKey,
                        result.data.accounts
                    ).map {
                        selectedAccount.clear()
                        CreateAccountRenderAction.NavigateToAccountList
                    }
                }
            } else {
                Single.just(CreateAccountRenderAction.OnImportKeyError(
                    context().getString(R.string.issue_create_account_import_key_error_body)
                ))
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnImportKeyProgress)
    }

    private fun getAvailableGooglePlaySku(billingRequest: BillingRequest): Observable<CreateAccountRenderAction> {
        return billingRequest
            .getCreateAccountSku()
            .map<CreateAccountRenderAction> { skuDetails ->
                CreateAccountRenderAction.OnSkuSuccess(skuDetails)
            }
            .onErrorReturn { billingError ->
                getErrors(billingError as BillingError)
            }
            .toObservable()
            .startWith(CreateAccountRenderAction.OnSkuProgress)
    }

    private fun getErrors(error: BillingError): CreateAccountRenderAction = when (error) {
        BillingError.BillingSetupConnectionFailed -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_billing_setup_error))
        }
        BillingError.BillingSetupFailed -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_billing_setup_error))
        }
        BillingError.SkuBillingUnavailable -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_billing_setup_error))
        }
        BillingError.SkuNotFound -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_sku_error))
        }
        BillingError.SkuNotAvailable -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_sku_error))
        }
        BillingError.SkuRequestFailed -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_billing_setup_error))
        }
        BillingError.SkuAlreadyOwned -> {
            CreateAccountRenderAction.OnGetSkuError(
                context().getString(R.string.issue_create_account_billing_setup_error))
        }
    }
}
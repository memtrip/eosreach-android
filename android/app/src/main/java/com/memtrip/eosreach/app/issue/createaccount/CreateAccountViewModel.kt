package com.memtrip.eosreach.app.issue.createaccount

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountError
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.db.sharedpreferences.SelectedAccount
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
        CreateAccountIntent.Init -> Observable.just(CreateAccountRenderAction.OnSkuProgress)
        is CreateAccountIntent.BillingSetupSuccess -> Observable.just(CreateAccountRenderAction.OnSkuSuccess(intent.skuDetails))
        is CreateAccountIntent.BillingSetupFailed -> Observable.just(getErrors(intent.billingError))
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
            view = CreateAccountViewState.View.OnCreateAccountSuccess(renderAction.purchaseToken, renderAction.privateKey))
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
        return keyManager.createEosPrivateKey().flatMap { eosPrivateKey ->
            keyManager.importPrivateKey(eosPrivateKey).flatMap { publicKey ->
                eosCreateAccountRequest.createAccount(
                    purchaseToken,
                    accountName,
                    publicKey
                ).flatMap<CreateAccountRenderAction> { result ->
                    if (result.success) {
                        unusedBillingPurchaseId.clear()
                        createAccountSuccess(purchaseToken, publicKey)
                    } else {
                        Single.just(createAccountError(result.apiError!!))
                    }
                }.onErrorReturn {
                    CreateAccountRenderAction.OnCreateAccountError(
                        context().getString(R.string.issue_create_account_generic_error))
                }
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnCreateAccountProgress)
    }

    private fun createAccountSuccess(purchaseToken: String, publicKey: String): Single<CreateAccountRenderAction> {
        return keyManager.getPrivateKey(publicKey).map<CreateAccountRenderAction> { privateKey ->
            CreateAccountRenderAction.OnCreateAccountSuccess(purchaseToken, privateKey.toString())
        }.onErrorReturn {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_fatal_error))
        }
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
        return keyManager.createEosPrivateKey(privateKey).flatMap { eosPrivateKey ->
            accountForPublicKeyRequest.getAccountsForKey(eosPrivateKey.publicKey.toString()).flatMap { result ->
                if (result.success) {
                    if (result.data!!.accounts.isEmpty()) {
                        Single.just(CreateAccountRenderAction.OnImportKeyError(
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
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnImportKeyProgress)
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
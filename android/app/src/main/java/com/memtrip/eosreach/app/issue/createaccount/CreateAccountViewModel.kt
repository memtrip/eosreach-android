package com.memtrip.eosreach.app.issue.createaccount

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountNameSystemBalance
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountError
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.billing.BillingFlowResponse
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.db.sharedpreferences.AccountListSelection
import com.memtrip.eosreach.db.sharedpreferences.UnusedBillingPurchaseToken
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKey
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKeyInLimbo
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKeyNoAccountsSynced
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single

abstract class CreateAccountViewModel(
    private val keyManager: EosKeyManager,
    private val eosCreateAccountRequest: EosCreateAccountRequest,
    private val accountForPublicKeyRequest: AccountForPublicKeyRequest,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val accountListSelection: AccountListSelection,
    private val unusedPublicKeyInLimbo: UnusedPublicKeyInLimbo,
    private val unusedBillingPurchaseToken: UnusedBillingPurchaseToken,
    private val unusedPublicKey: UnusedPublicKey,
    private val unusedPublicKeyNoAccountsSynced: UnusedPublicKeyNoAccountsSynced,
    application: Application
) : MxViewModel<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState>(
    CreateAccountViewState(view = CreateAccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CreateAccountIntent): Observable<CreateAccountRenderAction> = when (intent) {
        CreateAccountIntent.Idle -> Observable.just(CreateAccountRenderAction.Idle)
        CreateAccountIntent.Init -> checkUnUsedPublicKeyState()
        CreateAccountIntent.StartBillingConnection -> Observable.just(CreateAccountRenderAction.StartBillingConnection)
        is CreateAccountIntent.BillingSetupSuccess -> Observable.just(CreateAccountRenderAction.OnSkuSuccess(intent.skuDetails))
        is CreateAccountIntent.BillingSetupFailed -> Observable.just(getErrors(intent.billingError))
        is CreateAccountIntent.BillingFlowComplete -> handleBillingFlowResponse(intent.accountName, intent.billingResponse)
        is CreateAccountIntent.BuyAccount -> Observable.just(validateAccountName(intent.accountName))
        is CreateAccountIntent.CreateAccount -> createAccount(intent.purchaseToken, intent.accountName)
        is CreateAccountIntent.CreateAccountError -> Observable.just(CreateAccountRenderAction.OnCreateAccountError(intent.error))
        CreateAccountIntent.RetryLimbo -> verifyAccountsForPublicKey(unusedPublicKey.get()).toObservable()
            .startWith(CreateAccountRenderAction.OnCreateAccountLimboProgress)
        CreateAccountIntent.Done -> getAccountsForKey()
    }

    override fun reducer(previousState: CreateAccountViewState, renderAction: CreateAccountRenderAction): CreateAccountViewState = when (renderAction) {
        CreateAccountRenderAction.Idle -> previousState.copy(
            view = CreateAccountViewState.View.Idle)
        CreateAccountRenderAction.StartBillingConnection -> previousState.copy(
            view = CreateAccountViewState.View.StartBillingConnection)
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
        CreateAccountRenderAction.OnCreateAccountLimbo -> previousState.copy(
            view = CreateAccountViewState.View.OnCreateAccountLimbo)
        CreateAccountRenderAction.OnCreateAccountLimboProgress -> previousState.copy(
            view = CreateAccountViewState.View.OnCreateAccountLimboProgress)
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

    private fun checkUnUsedPublicKeyState(): Observable<CreateAccountRenderAction> {
        return if (unusedPublicKeyInLimbo.get()) {
            Observable.just(CreateAccountRenderAction.OnCreateAccountLimbo)
        } else if (unusedPublicKeyNoAccountsSynced.get()) {
            Observable.just<CreateAccountRenderAction>(CreateAccountRenderAction.OnImportKeyError(
                context().getString(R.string.issue_create_account_import_key_no_accounts)
            ))
        } else {
            Observable.just<CreateAccountRenderAction>(CreateAccountRenderAction.StartBillingConnection)
        }
    }

    private fun handleBillingFlowResponse(
        accountName: String,
        billingResponse: BillingFlowResponse
    ): Observable<CreateAccountRenderAction> = when (billingResponse) {
        is BillingFlowResponse.Success -> createAccount(billingResponse.purchaseToken, accountName)
        BillingFlowResponse.UserCancelledFlow -> {
            billingFlowError(context().getString(R.string.issue_create_account_billing_purchase_cancelled_error))
        }
        BillingFlowResponse.ItemAlreadyOwned -> {
            val purchaseToken = unusedBillingPurchaseToken.get()
            if (purchaseToken.isNotEmpty()) {
                createAccount(purchaseToken, accountName)
            } else {
                billingFlowError(context().getString(R.string.issue_create_account_billing_purchase_fatal_error))
            }
        }
        BillingFlowResponse.Error -> {
            billingFlowError(context().getString(R.string.issue_create_account_billing_purchase_failed_error))
        }
    }

    private fun billingFlowError(message: String): Observable<CreateAccountRenderAction> {
        return Observable.just(CreateAccountRenderAction.OnCreateAccountError(message))
    }

    private fun validateAccountName(accountName: String): CreateAccountRenderAction {
        return if (accountName.isEmpty() || accountName.length != 12) {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_username_validation_error))
        } else if (Character.isDigit(accountName[0])) {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_username_number_start_validation_error))
        } else {
            CreateAccountRenderAction.OnAccountNameValidationPassed
        }
    }

    private fun createAccount(
        purchaseToken: String,
        accountName: String
    ): Observable<CreateAccountRenderAction> {
        unusedBillingPurchaseToken.put(purchaseToken)
        return privateKeyForNewAccount().flatMap { publicKey ->
            eosCreateAccountRequest.createAccount(
                purchaseToken,
                accountName,
                publicKey
            ).flatMap<CreateAccountRenderAction> { result ->
                if (result.success) {
                    createAccountSuccess(purchaseToken, publicKey)
                } else {
                    if (result.apiError!! == EosCreateAccountError.GenericError) {
                        verifyAccountsForPublicKey(publicKey)
                    } else {
                        Single.just(createAccountError(result.apiError))
                    }
                }
            }.onErrorResumeNext {
                verifyAccountsForPublicKey(publicKey)
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnCreateAccountProgress)
    }

    /**
     * A user might lose internet connection while creating an account, in this case the
     * account might have been created, so we check accounts for the public key. If the accounts
     * for public key request fails, the create account process is in limbo.
     */
    private fun verifyAccountsForPublicKey(publicKey: String): Single<CreateAccountRenderAction> {
        return accountForPublicKeyRequest.getAccountsForKey(publicKey).flatMap { result ->
            if (result.success) {
                if (result.data!!.accounts.isNotEmpty()) {
                    createAccountSuccess(
                        unusedBillingPurchaseToken.get(),
                        unusedPublicKey.get())
                } else {
                    /**
                     * If no accounts are returned, we can safely assume that
                     * the account was not created and it was a legit error.
                     */
                    unusedPublicKeyInLimbo.clear()
                    Single.just(createAccountError(EosCreateAccountError.GenericError))
                }
            } else {
                /**
                 * If we cannot verify whether the current session public key has any
                 * accounts, we will display an error to the user. The user must get a
                 * successful response from accountForPublicKeyRequest before continuing.
                 */
                unusedPublicKeyInLimbo.put(true)
                Single.just(CreateAccountRenderAction.OnCreateAccountLimbo)
            }
        }
    }

    /**
     * If an error occurs while issuing the account, we don't want to continue creating
     * unused private keys.
     */
    private fun privateKeyForNewAccount(): Single<String> {
        return if (unusedPublicKey.exists()) {
            keyManager.getPrivateKey(unusedPublicKey.get()).flatMap { privateKey ->
                Single.just(privateKey.publicKey.toString())
            }
        } else {
            keyManager.createEosPrivateKey().flatMap { privateKey ->
                unusedPublicKey.put(privateKey.publicKey.toString())
                keyManager.importPrivateKey(privateKey)
            }
        }
    }

    private fun createAccountSuccess(purchaseToken: String, publicKey: String): Single<CreateAccountRenderAction> {
        unusedPublicKeyInLimbo.clear()
        unusedBillingPurchaseToken.clear()
        unusedPublicKeyNoAccountsSynced.put(true)
        return keyManager.getPrivateKey(publicKey).map<CreateAccountRenderAction> { privateKey ->
            CreateAccountRenderAction.OnCreateAccountSuccess(purchaseToken, privateKey.toString())
        }.onErrorReturn {
            CreateAccountRenderAction.OnCreateAccountError(
                context().getString(R.string.issue_create_account_fatal_error))
        }
    }

    private fun createAccountError(error: EosCreateAccountError): CreateAccountRenderAction = when (error) {
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

    private fun getAccountsForKey(): Observable<CreateAccountRenderAction> {
        return keyManager.getPrivateKey(unusedPublicKey.get()).flatMap { eosPrivateKey ->
            accountForPublicKeyRequest.getAccountsForKey(eosPrivateKey.publicKey.toString()).flatMap { result ->
                if (result.success) {
                    if (result.data!!.accounts.isEmpty()) {
                        Single.just(CreateAccountRenderAction.OnImportKeyError(
                            context().getString(R.string.issue_create_account_import_key_no_accounts)))
                    } else {
                        insertAccounts(
                            result.data.publicKey,
                            result.data.accounts)
                    }
                } else {
                    Single.just(CreateAccountRenderAction.OnImportKeyError(
                        context().getString(R.string.issue_create_account_import_key_error_body)
                    ))
                }
            }
        }.toObservable().startWith(CreateAccountRenderAction.OnImportKeyProgress)
    }

    private fun insertAccounts(publicKey: String, accounts: List<AccountNameSystemBalance>): Single<CreateAccountRenderAction> {
        return insertAccountsForPublicKey.replace(
            publicKey,
            accounts
        ).map {
            accountListSelection.clear()
            unusedPublicKey.clear()
            unusedPublicKeyNoAccountsSynced.clear()
            CreateAccountRenderAction.NavigateToAccountList
        }
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
package com.memtrip.eosreach.app.issue.importkey

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.accountforkey.AccountForKeyError

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable

abstract class ImportKeyViewModel(
    private val importKeyUseCase: ImportKeyUseCase,
    application: Application
) : MxViewModel<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState>(
    ImportKeyViewState(view = ImportKeyViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ImportKeyIntent): Observable<ImportKeyRenderAction> = when (intent) {
        is ImportKeyIntent.Init -> Observable.just(ImportKeyRenderAction.Idle)
        is ImportKeyIntent.ImportKey -> importKey(intent.privateKey)
        ImportKeyIntent.ViewSource -> Observable.just(ImportKeyRenderAction.NavigateToGithubSource)
    }

    override fun reducer(previousState: ImportKeyViewState, renderAction: ImportKeyRenderAction): ImportKeyViewState = when (renderAction) {
        ImportKeyRenderAction.Idle -> previousState.copy(view = ImportKeyViewState.View.Idle)
        ImportKeyRenderAction.OnProgress -> previousState.copy(view = ImportKeyViewState.View.OnProgress)
        ImportKeyRenderAction.OnSuccess -> previousState.copy(view = ImportKeyViewState.View.OnSuccess)
        is ImportKeyRenderAction.OnError -> previousState.copy(view = ImportKeyViewState.View.OnError(renderAction.error))
        ImportKeyRenderAction.NavigateToGithubSource -> previousState.copy(view = ImportKeyViewState.View.NavigateToGithubSource)
    }

    private fun importKey(privateKey: String): Observable<ImportKeyRenderAction> {
        return importKeyUseCase
            .importKey(privateKey)
            .map {
                if (it.success) {
                    ImportKeyRenderAction.OnSuccess
                } else {
                    onError(it.apiError!!)
                }
            }
            .toObservable()
            .startWith(ImportKeyRenderAction.OnProgress)
    }

    private fun onError(error: AccountForKeyError): ImportKeyRenderAction.OnError = when (error) {
        AccountForKeyError.Generic -> ImportKeyRenderAction.OnError(
            context().getString(R.string.issue_import_key_error_generic))
        AccountForKeyError.InvalidPrivateKey -> ImportKeyRenderAction.OnError(
            context().getString(R.string.issue_import_key_error_invalid_private_key_format))
        is AccountForKeyError.FailedRetrievingAccountList -> ImportKeyRenderAction.OnError(
            context().getString(R.string.issue_import_key_error_generic))
        AccountForKeyError.NoAccounts -> ImportKeyRenderAction.OnError(
            context().getString(R.string.issue_import_key_error_no_accounts))
        AccountForKeyError.PrivateKeyAlreadyImported -> ImportKeyRenderAction.OnError(
            context().getString(R.string.issue_import_key_private_key_already_imported))
    }
}
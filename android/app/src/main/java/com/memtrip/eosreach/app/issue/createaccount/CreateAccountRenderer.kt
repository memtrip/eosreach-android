package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CreateAccountRenderAction : MxRenderAction {
    object Idle : CreateAccountRenderAction()
    object OnSkuProgress : CreateAccountRenderAction()
    data class OnSkuSuccess(val skuDetails: SkuDetails) : CreateAccountRenderAction()
    data class OnGetSkuError(val message: String) : CreateAccountRenderAction()
    object OnAccountNameValidationPassed : CreateAccountRenderAction()
    object OnCreateAccountProgress : CreateAccountRenderAction()
    data class OnCreateAccountSuccess(val purchaseToken: String, val privateKey: String) : CreateAccountRenderAction()
    data class OnCreateAccountError(val error: String) : CreateAccountRenderAction()
    object OnImportKeyProgress : CreateAccountRenderAction()
    data class OnImportKeyError(val error: String) : CreateAccountRenderAction()
    object NavigateToAccountList : CreateAccountRenderAction()
}

interface CreateAccountViewLayout : MxViewLayout {
    fun showSkuProgress()
    fun showSkuSuccess(skuDetails: SkuDetails)
    fun showSkuError(message: String)
    fun showAccountNameValidationPassed()
    fun showCreateAccountProgress()
    fun showAccountCreated(purchaseToken: String,privateKey: String)
    fun showCreateAccountError(error: String)
    fun showImportKeyProgress()
    fun showImportKeyError(error: String)
    fun navigateToAccountList()
}

class CreateAccountViewRenderer @Inject internal constructor() : MxViewRenderer<CreateAccountViewLayout, CreateAccountViewState> {
    override fun layout(layout: CreateAccountViewLayout, state: CreateAccountViewState) = when (state.view) {
        CreateAccountViewState.View.Idle -> {
        }
        CreateAccountViewState.View.OnSkuProgress -> {
            layout.showSkuProgress()
        }
        is CreateAccountViewState.View.OnSkuSuccess -> {
            layout.showSkuSuccess(state.view.skuDetails)
        }
        is CreateAccountViewState.View.OnGetSkuError -> {
            layout.showSkuError(state.view.message)
        }
        is CreateAccountViewState.View.OnAccountNameValidationPassed -> {
            layout.showAccountNameValidationPassed()
        }
        CreateAccountViewState.View.OnCreateAccountProgress -> {
            layout.showCreateAccountProgress()
        }
        is CreateAccountViewState.View.OnCreateAccountSuccess -> {
            layout.showAccountCreated(state.view.purchaseToken, state.view.privateKey)
        }
        is CreateAccountViewState.View.CreateAccountError -> {
            layout.showCreateAccountError(state.view.error)
        }
        CreateAccountViewState.View.OnImportKeyProgress -> {
            layout.showImportKeyProgress()
        }
        is CreateAccountViewState.View.ImportKeyError -> {
            layout.showImportKeyError(state.view.error)
        }
        CreateAccountViewState.View.NavigateToAccountList -> {
            layout.navigateToAccountList()
        }
    }
}
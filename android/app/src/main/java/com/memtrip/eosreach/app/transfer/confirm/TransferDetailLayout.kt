package com.memtrip.eosreach.app.transfer.confirm

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.app.price.CurrencyPairFormatter
import com.memtrip.eosreach.uikit.visible

import kotlinx.android.synthetic.main.transfer_details_layout.view.*

class TransferDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.transfer_details_layout, this)
    }

    fun populate(
        transferAmount: Balance,
        to: String,
        from: String,
        memo: String,
        contractAccountBalance: ContractAccountBalance
    ) {

        val currencyPairBalance = CurrencyPairFormatter.formatAmountCurrencyPairValue(
            transferAmount.amount, contractAccountBalance.exchangeRate)

        val formattedAmount = BalanceFormatter.formatEosBalance(transferAmount)

        if (currencyPairBalance.isNotEmpty()) {
            populateFormattedAmountAndCurrencyPairing(formattedAmount, currencyPairBalance)
        } else {
            populateFormattedAmount(formattedAmount)
        }

        populateBasicDetails(to, from, memo)
    }

    private fun populateFormattedAmountAndCurrencyPairing(
        formattedAmount: String,
        currencyPairBalance: String
    ) {
        transfer_details_amount_value.text = context.getString(
            R.string.transfer_confirm_crypto_amount_with_currency_pair,
            formattedAmount,
            currencyPairBalance)
    }

    private fun populateFormattedAmount(formattedAmount: String) {
        transfer_details_amount_value.text = context.getString(
            R.string.transfer_confirm_crypto_amount,
            formattedAmount)
    }

    fun populateDate(formattedDateString: String) {
        transfer_details_date_group.visible()
        transfer_details_date_value.text = formattedDateString
    }

    private fun populateBasicDetails(
        to: String,
        from: String,
        memo: String
    ) {
        transfer_details_to_value.text = to
        transfer_details_from_value.text = from
        transfer_details_memo_value.text = memo
    }
}
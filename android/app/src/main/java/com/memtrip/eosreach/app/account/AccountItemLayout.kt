package com.memtrip.eosreach.app.account

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.account.AccountEntity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class AccountItemLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var accountEntity: AccountEntity

    init {
        LayoutInflater.from(context).inflate(R.layout.account_list_item, this)
    }

    fun populate(accountEntity: AccountEntity) {
        this.accountEntity = accountEntity

        accounts_list_item_account_name.text = accountEntity.accountName

        if (accountEntity.symbol != null && accountEntity.balance != null) {
            accounts_list_item_balance.text = context.getString(
                R.string.accounts_list_item_balance,
                with(DecimalFormat("0.0000")) {
                    roundingMode = RoundingMode.CEILING
                    this
                }.format(accountEntity.balance),
                accountEntity.symbol)
        } else {
            accounts_list_item_balance.text = context.getString(
                R.string.accounts_list_item_balance_empty)
        }
    }

    fun clicks(): Observable<Any> = RxView.clicks(accounts_list_item_container)
}
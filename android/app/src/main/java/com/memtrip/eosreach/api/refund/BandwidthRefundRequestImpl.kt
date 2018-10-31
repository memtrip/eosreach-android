/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.api.refund

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.account.RefundChain
import com.memtrip.eos.core.crypto.EosPrivateKey

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class BandwidthRefundRequestImpl @Inject internal constructor(
    private val refundChain: RefundChain,
    private val rxSchedulers: RxSchedulers
) : BandwidthRefundRequest {

    override fun requestRefund(
        accountName: String,
        authorizingPrivateKey: EosPrivateKey,
        transactionExpiry: Date
    ): Single<Result<ActionReceipt, BandwidthRefundError>> {
        return refundChain.refund(TransactionContext(accountName, authorizingPrivateKey, transactionExpiry))
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map { response ->
                if (response.isSuccessful) {
                    Result(ActionReceipt(response.body!!.transaction_id, accountName))
                } else {
                    Result<ActionReceipt, BandwidthRefundError>(
                        BandwidthRefundError.TransactionError(response.errorBody!!))
                }
            }
            .onErrorReturn {
                it.printStackTrace()
                Result(BandwidthRefundError.GenericError)
            }
    }
}
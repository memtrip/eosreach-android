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
package com.memtrip.eosreach.api.eoscreateaccount

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import com.squareup.moshi.Moshi
import io.reactivex.Single

import javax.inject.Inject

class EosCreateAccountRequestImpl @Inject constructor(
    private val eosCreateAccountApi: EosCreateAccountApi,
    private val moshi: Moshi,
    private val rxSchedulers: RxSchedulers
) : EosCreateAccountRequest {

    override fun createAccount(
        purchaseToken: String,
        accountName: String,
        publicKey: String
    ): Single<Result<CreateAccountReceipt, EosCreateAccountError>> {
        return eosCreateAccountApi.createAccount(CreateAccountRequest(
            purchaseToken,
            accountName.toLowerCase(),
            publicKey
        )).observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background()).map { response ->
                if (response.isSuccessful) {
                    Result(CreateAccountReceipt(response.body()!!.transactionId))
                } else {
                    if (response.errorBody() != null) {
                        try {
                            val createAccountError = moshi.adapter(CreateAccountError::class.java)
                                .fromJson(response.errorBody()!!.string())

                            when (createAccountError!!.errorCode) {
                                "PUBLIC_KEY_INVALID_FORMAT" -> {
                                    Result<CreateAccountReceipt, EosCreateAccountError>(EosCreateAccountError.FatalError)
                                }
                                "ACCOUNT_NAME_EXISTS" -> {
                                    Result<CreateAccountReceipt, EosCreateAccountError>(EosCreateAccountError.AccountNameExists)
                                }
                                else -> {
                                    Result<CreateAccountReceipt, EosCreateAccountError>(EosCreateAccountError.GenericError)
                                }
                            }
                        } catch (e: Exception) {
                            Result<CreateAccountReceipt, EosCreateAccountError>(EosCreateAccountError.GenericError)
                        }
                    } else {
                        Result<CreateAccountReceipt, EosCreateAccountError>(EosCreateAccountError.GenericError)
                    }
                }
        }
    }
}
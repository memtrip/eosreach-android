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
package com.memtrip.eosreach.app.settings.eosendpoint

import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.model.info.Info
import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import okhttp3.OkHttpClient

import javax.inject.Inject

class EosEndpointUseCase @Inject internal constructor(
    private val okHttpClient: OkHttpClient,
    private val rxSchedulers: RxSchedulers
) {

    fun getInfo(endpointUrl: String): Single<Result<Info, GetInfoError>> {
        return Api(endpointUrl, okHttpClient)
            .chain
            .getInfo()
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map {
                Result<Info, GetInfoError>(it.body()!!)
            }
            .onErrorReturn {
                Result(GetInfoError())
            }
    }

    class GetInfoError : ApiError
}
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
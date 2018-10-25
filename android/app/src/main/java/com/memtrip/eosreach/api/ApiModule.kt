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
package com.memtrip.eosreach.api

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.memtrip.eos.chain.actions.query.bandwidth.GetDelegatedBandwidth
import com.memtrip.eos.chain.actions.query.producer.GetBlockProducers
import com.memtrip.eos.chain.actions.query.proxy.GetRegProxyInfo
import com.memtrip.eos.chain.actions.query.ramprice.GetRamPrice
import com.memtrip.eos.chain.actions.transaction.account.BuyRamBytesChain
import com.memtrip.eos.chain.actions.transaction.account.DelegateBandwidthChain
import com.memtrip.eos.chain.actions.transaction.account.SellRamChain
import com.memtrip.eos.chain.actions.transaction.account.UnDelegateBandwidthChain
import com.memtrip.eos.chain.actions.transaction.transfer.TransferChain
import com.memtrip.eos.chain.actions.transaction.vote.VoteChain

import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountApi
import com.memtrip.eosreach.api.eosprice.EosPriceApi
import com.memtrip.eosreach.db.sharedpreferences.EosEndpoint
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
internal object ApiModule {

    @JvmStatic
    @Provides
    fun okHttpClient(apiConfig: ApiConfig): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)

        apiConfig.interceptors.map {
            okHttpClient.addInterceptor(it)
        }

        return okHttpClient.build()
    }

    @JvmStatic
    @Provides
    fun moshi(): Moshi = Moshi.Builder().build()

    @JvmStatic
    @Provides
    fun moshiConverterFactory(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @JvmStatic
    @Provides
    fun retrofit(application: Application, okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(application.getString(R.string.app_default_utility_endpoint_root))
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converterFactory)
            .build()
    }

    @JvmStatic
    @Provides
    fun api(okHttpClient: OkHttpClient, eosEndpoint: EosEndpoint): Api {
        return Api(eosEndpoint.get(), okHttpClient)
    }

    @JvmStatic
    @Provides
    fun historyApi(api: Api): HistoryApi = api.history

    @JvmStatic
    @Provides
    fun chainApi(api: Api): ChainApi = api.chain

    @JvmStatic
    @Provides
    fun transferChain(chainApi: ChainApi): TransferChain = TransferChain(chainApi)

    @JvmStatic
    @Provides
    fun getBlockProducers(chainApi: ChainApi): GetBlockProducers = GetBlockProducers(chainApi)

    @JvmStatic
    @Provides
    fun getRegProxyInfo(chainApi: ChainApi): GetRegProxyInfo = GetRegProxyInfo(chainApi)

    @JvmStatic
    @Provides
    fun getRamPrice(chainApi: ChainApi): GetRamPrice = GetRamPrice(chainApi)

    @JvmStatic
    @Provides
    fun voteChain(chainApi: ChainApi): VoteChain = VoteChain(chainApi)

    @JvmStatic
    @Provides
    fun delegateBandwidthChain(chainApi: ChainApi): DelegateBandwidthChain = DelegateBandwidthChain(chainApi)

    @JvmStatic
    @Provides
    fun unDelegateBandwidthChain(chainApi: ChainApi): UnDelegateBandwidthChain = UnDelegateBandwidthChain(chainApi)

    @JvmStatic
    @Provides
    fun buyRamBytesChain(chainApi: ChainApi): BuyRamBytesChain = BuyRamBytesChain(chainApi)

    @JvmStatic
    @Provides
    fun getBandwidth(chainApi: ChainApi): GetDelegatedBandwidth = GetDelegatedBandwidth(chainApi)

    @JvmStatic
    @Provides
    fun sellRamBytesChain(chainApi: ChainApi): SellRamChain = SellRamChain(chainApi)

    @JvmStatic
    @Provides
    fun eosPriceApi(retrofit: Retrofit): EosPriceApi = retrofit.create(EosPriceApi::class.java)

    @JvmStatic
    @Provides
    fun eosCreateAccountApi(retrofit: Retrofit): EosCreateAccountApi = retrofit.create(EosCreateAccountApi::class.java)
}
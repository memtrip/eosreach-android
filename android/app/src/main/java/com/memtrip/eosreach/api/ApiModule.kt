package com.memtrip.eosreach.api

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.memtrip.eos.http.aggregation.producer.GetBlockProducersAggregate

import com.memtrip.eos.http.aggregation.transfer.TransferAggregate
import com.memtrip.eos.http.aggregation.vote.VoteAggregate
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
    fun transferAggregate(chainApi: ChainApi): TransferAggregate = TransferAggregate(chainApi)

    @JvmStatic
    @Provides
    fun getBlockProducersAggregate(chainApi: ChainApi): GetBlockProducersAggregate {
        return GetBlockProducersAggregate(chainApi)
    }

    @JvmStatic
    @Provides
    fun voteAggregate(chainApi: ChainApi): VoteAggregate = VoteAggregate(chainApi)

    @JvmStatic
    @Provides
    fun eosPriceApi(retrofit: Retrofit): EosPriceApi = retrofit.create(EosPriceApi::class.java)

    @JvmStatic
    @Provides
    fun eosCreateAccountApi(retrofit: Retrofit): EosCreateAccountApi = retrofit.create(EosCreateAccountApi::class.java)
}
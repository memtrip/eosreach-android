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
import com.memtrip.eosreach.api.eosprice.EosPriceApi
import com.memtrip.eosreach.db.EosEndpoint
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
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        apiConfig.interceptors.map {
            okHttpClient.addInterceptor(it)
        }

        return okHttpClient.build()
    }

    @JvmStatic
    @Provides
    fun moshi(): Converter.Factory {
        return MoshiConverterFactory.create(Moshi.Builder().build())
    }

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
    fun api(application: Application, okHttpClient: OkHttpClient, eosEndpoint: EosEndpoint): Api {
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
    fun eosPriceApi(retrofit: Retrofit): EosPriceApi {
        return retrofit.create(EosPriceApi::class.java)
    }
}
package com.memtrip.eosreach.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eosreach.api.eosprice.EosPriceApi
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
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @JvmStatic
    @Provides
    fun moshi(): Converter.Factory {
        return MoshiConverterFactory.create(Moshi.Builder().build())
    }

    @JvmStatic
    @Provides
    fun retrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converterFactory)
            .build()
    }

    @JvmStatic
    @Provides
    fun api(okHttpClient: OkHttpClient): Api {
        return Api("http://f24357e7.ngrok.io/", okHttpClient)
    }

    @JvmStatic
    @Provides
    fun historyApi(api: Api): HistoryApi = api.history

    @JvmStatic
    @Provides
    fun chainApi(api: Api): ChainApi = api.chain

    @JvmStatic
    @Provides
    fun eosPriceApi(retrofit: Retrofit): EosPriceApi {
        return retrofit.create(EosPriceApi::class.java)
    }
}
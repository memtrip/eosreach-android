package com.memtrip.eosreach.api

import com.memtrip.eos.http.rpc.Api
import com.memtrip.eos.http.rpc.HistoryApi

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun api(okHttpClient: OkHttpClient): Api {
        return Api("http://localhost:8888/", okHttpClient)
    }

    @JvmStatic
    @Provides
    fun historyApi(api: Api): HistoryApi = api.history
}
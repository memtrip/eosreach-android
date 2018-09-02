package com.memtrip.eosreach.api

import okhttp3.Interceptor

data class ApiConfig(
    val interceptors: List<Interceptor> = emptyList()
)
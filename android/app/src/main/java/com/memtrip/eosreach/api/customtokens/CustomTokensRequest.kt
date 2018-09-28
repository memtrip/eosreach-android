package com.memtrip.eosreach.api.customtokens

import io.reactivex.Single
import java.io.IOException

interface CustomTokensRequest {
    fun getCustomTokens(): Single<TokenParent>
}

class NoAirdropsFound : IOException()
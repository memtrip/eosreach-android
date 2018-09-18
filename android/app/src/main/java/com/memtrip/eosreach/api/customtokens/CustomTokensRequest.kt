package com.memtrip.eosreach.api.customtokens

import io.reactivex.Single

interface CustomTokensRequest {
    fun getCustomTokens(): Single<TokenParent>
}
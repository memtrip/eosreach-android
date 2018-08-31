package com.memtrip.eosreach.service

import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient

import io.vertx.core.Handler
import io.vertx.core.Vertx

class Price(
    private val vertx: Vertx
) {

    val handler: Handler<RoutingContext> = Handler { req ->

        val currency = req.pathParam("currency")

        if (currency == null || currency.length != 3) {
            req.response().statusCode = 400
            req.response().end()
        } else {

            WebClient.create(vertx)
                .get(443, "min-api.cryptocompare.com", "/data/price?fsym=EOS&tsyms=$currency")
                .ssl(true)
                .send { result ->
                    if (result.succeeded()) {
                        val response = result.result()

                        val priceJson = JsonObject(response.body())

                        if (JsonObject(response.body()).containsKey(currency)) {
                            req.response().endWithJson(Response(priceJson.getDouble(currency), currency))
                        } else {
                            req.response().statusCode = 400
                            req.response().end()
                        }
                    } else {
                        req.response().statusCode = 400
                        req.response().end()
                    }
                }
        }
    }

    private data class Response(
        val value: Double,
        val currency: String
    )
}
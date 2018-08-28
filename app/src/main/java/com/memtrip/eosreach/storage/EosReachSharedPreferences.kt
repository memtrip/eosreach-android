package com.memtrip.eosreach.storage

import android.app.Application
import android.content.Context
import com.memtrip.eosreach.R
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class EosReachSharedPreferences @Inject constructor(
    application: Application
) {

    private val sharedPreferences = application.getSharedPreferences(
        application.getString(R.string.app_shared_preferences_package),
        Context.MODE_PRIVATE)

    fun accountCreated(): Single<Boolean> {
        return Single.create<Boolean> {
            it.onSuccess(sharedPreferences.getBoolean("account_imported", false))
        }
    }

    fun saveAccountCreated(): Completable {
        return Completable.create {
            sharedPreferences.edit().putBoolean("account_imported", true).apply()
            it.onComplete()
        }
    }
}
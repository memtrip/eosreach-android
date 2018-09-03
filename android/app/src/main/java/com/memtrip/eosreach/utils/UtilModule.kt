package com.memtrip.eosreach.utils

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
internal object UtilModule {

    @JvmStatic
    @Provides
    fun rxSchedulers(): RxSchedulers {
        return object : RxSchedulers {
            override fun main(): Scheduler = AndroidSchedulers.mainThread()
            override fun background(): Scheduler = Schedulers.io()
        }
    }
}
package com.memtrip.eosreach.utils

import io.reactivex.Scheduler

interface RxSchedulers {
    fun main(): Scheduler
    fun background(): Scheduler
}
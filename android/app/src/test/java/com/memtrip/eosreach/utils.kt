package com.memtrip.eosreach

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestRxSchedulers : RxSchedulers {
    override fun main(): Scheduler = Schedulers.trampoline()
    override fun background(): Scheduler = Schedulers.trampoline()
}
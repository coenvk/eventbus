package com.coenvk.android.eventbus.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {

    val io: Scheduler
        get() = Schedulers.io()

    val main: Scheduler
        get() = AndroidSchedulers.mainThread()

    val ui: Scheduler
        get() = AndroidSchedulers.mainThread()

}

internal object DefaultSchedulerProvider : SchedulerProvider
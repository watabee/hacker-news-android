package com.github.watabee.hackernews

import io.reactivex.Scheduler

data class AppRxSchedulers(
    val io: Scheduler,
    val main: Scheduler,
    val computation: Scheduler
)
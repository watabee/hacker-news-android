package com.github.watabee.hackernews.workermanager

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WorkManagerModule {

    @ContributesAndroidInjector
    internal abstract fun contributesWorkManagerInitializer(): WorkManagerInitializer
}
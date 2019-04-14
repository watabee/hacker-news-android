package com.github.watabee.hackernews.di

import com.github.watabee.hackernews.HackerNewsApplication
import com.github.watabee.hackernews.api.ApiModule
import com.github.watabee.hackernews.cachedeleter.DeleteCacheModule
import com.github.watabee.hackernews.db.di.DbModule
import com.github.watabee.hackernews.workermanager.WorkManagerModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        AppModuleBinds::class,
        ActivityModule::class,
        ApiModule::class,
        DbModule::class,
        DeleteCacheModule::class,
        WorkManagerModule::class
    ]
)
interface AppComponent : AndroidInjector<HackerNewsApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<HackerNewsApplication>
}
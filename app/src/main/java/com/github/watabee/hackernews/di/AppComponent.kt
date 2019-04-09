package com.github.watabee.hackernews.di

import android.content.Context
import com.github.watabee.hackernews.HackerNewsApplication
import com.github.watabee.hackernews.api.ApiModule
import com.github.watabee.hackernews.db.di.DbModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppModuleBinds::class,
        ApiModule::class,
        DbModule::class
    ]
)
interface AppComponent {

    fun inject(application: HackerNewsApplication)

    fun activityComponentFactory(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

    companion object {
        fun create(appContext: Context): AppComponent =
            DaggerAppComponent.factory()
                .create(appContext)
    }
}
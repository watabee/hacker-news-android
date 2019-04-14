package com.github.watabee.hackernews.di

import androidx.lifecycle.ViewModelProvider
import com.github.watabee.hackernews.AppInitializer
import com.github.watabee.hackernews.appinitializers.TimberInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoSet
    abstract fun bindTimberInitializer(initializer: TimberInitializer): AppInitializer
}
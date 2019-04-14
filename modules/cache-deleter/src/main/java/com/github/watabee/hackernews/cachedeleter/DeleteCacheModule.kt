package com.github.watabee.hackernews.cachedeleter

import com.github.watabee.hackernews.AppInitializer
import com.github.watabee.hackernews.workermanager.ListenableWorkerFactory
import com.github.watabee.hackernews.workermanager.WorkerKey
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module(includes = [AssistedInject_DeleteCacheModule::class])
@AssistedModule
abstract class DeleteCacheModule {

    @Binds
    @IntoSet
    internal abstract fun bindDeleteCacheWorkRegistrar(
        registrar: DeleteCacheWorkRegistrar
    ): AppInitializer

    @Binds
    @IntoMap
    @WorkerKey(DeleteCacheWork::class)
    internal abstract fun bindDeleteCacheWorkFactory(
        factory: DeleteCacheWork.Factory
    ): ListenableWorkerFactory
}
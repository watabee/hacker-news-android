package com.github.watabee.hackernews.cachedeleter

import com.github.watabee.hackernews.workermanager.WorkerKey
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [AssistedInject_DeleteCacheModule::class])
@AssistedModule
abstract class DeleteCacheModule {

    @Binds
    @IntoMap
    @WorkerKey(DeleteCacheWork::class)
    internal abstract fun bindDeleteCacheWorkFactory(
        factory: DeleteCacheWork.Factory
    ): com.github.watabee.hackernews.workermanager.AppWorkerFactory
}
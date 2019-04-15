package com.github.watabee.hackernews.topstories

import com.github.watabee.hackernews.di.ActivityScope
import com.github.watabee.hackernews.di.FragmentScope
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TopStoriesActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [TopStoriesFragmentModule::class])
    abstract fun contributeTopStoriesActivity(): TopStoriesActivity
}

@Module
abstract class TopStoriesFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [TopStoriesAssistedInjectModule::class])
    abstract fun contributeTopStoriesFragment(): TopStoriesFragment
}

@AssistedModule
@Module(includes = [AssistedInject_TopStoriesAssistedInjectModule::class])
abstract class TopStoriesAssistedInjectModule
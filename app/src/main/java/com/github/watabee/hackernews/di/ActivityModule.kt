package com.github.watabee.hackernews.di

import com.github.watabee.hackernews.topstories.TopStoriesActivity
import com.github.watabee.hackernews.topstories.TopStoriesAssistedInjectModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [TopStoriesAssistedInjectModule::class])
    abstract fun contributeTopStoriesActivity(): TopStoriesActivity
}
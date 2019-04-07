package com.github.watabee.hackernews.di

import com.github.watabee.hackernews.topstories.TopStoriesAssistedInjectModule
import dagger.Module

@Module(
    includes = [
        TopStoriesAssistedInjectModule::class
    ]
)
class ActivityModule
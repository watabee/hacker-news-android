package com.github.watabee.hackernews.di

import com.github.watabee.hackernews.topstories.TopStoriesActivityModule
import dagger.Module

@Module(
    includes = [
        TopStoriesActivityModule::class
    ]
)
class ActivityModule
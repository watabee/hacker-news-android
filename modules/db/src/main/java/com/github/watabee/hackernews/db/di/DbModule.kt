package com.github.watabee.hackernews.db.di

import android.content.Context
import com.github.watabee.hackernews.db.AppDatabase
import com.github.watabee.hackernews.db.dao.StoryDao
import dagger.Module
import dagger.Provides

@Module
object DbModule {

    @Provides
    @JvmStatic
    internal fun provideStoryDao(context: Context): StoryDao =
        AppDatabase.getInstance(context).storyDao()
}
package com.github.watabee.hackernews.di

import androidx.appcompat.app.AppCompatActivity
import com.github.watabee.hackernews.HackerNewsApplication
import com.github.watabee.hackernews.topstories.TopStoriesActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: TopStoriesActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appCompatActivity: AppCompatActivity): ActivityComponent
    }

    companion object {
        fun create(activity: AppCompatActivity): ActivityComponent =
            (activity.application as HackerNewsApplication).appComponent
                .activityComponentFactory()
                .create(activity)
    }
}
package com.github.watabee.hackernews.di

import androidx.appcompat.app.AppCompatActivity
import com.github.watabee.hackernews.HackerNewsApplication
import com.github.watabee.hackernews.topstories.TopStoriesActivity
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: TopStoriesActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): ActivityComponent
    }

    companion object {
        fun create(activity: AppCompatActivity): ActivityComponent =
            (activity.application as HackerNewsApplication).appComponent
                .activityComponentBuilder()
                .build()
    }
}
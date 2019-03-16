package com.github.watabee.hackernews.common

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder

class StoriesAdapter : GroupAdapter<ViewHolder>() {

    private val storiesSection = Section()

    init {
        add(storiesSection)
    }

    fun update(stories: List<StoryUiModel>) {
        storiesSection.update(stories.map { StoryBindableItem(it) })
    }
}
package com.github.watabee.hackernews.topstories

import com.github.watabee.hackernews.common.StoryUiModel

data class TopStoriesState(
    val topStories: List<StoryUiModel>,
    val error: Boolean,
    val inProgress: Boolean
) {
    companion object {
        fun idle(): TopStoriesState =
            TopStoriesState(
                emptyList(),
                error = false,
                inProgress = false
            )
    }
}
package com.github.watabee.hackernews.common

import com.github.watabee.hackernews.R
import com.github.watabee.hackernews.databinding.ListItemStoryBinding
import com.xwray.groupie.databinding.BindableItem

class StoryBindableItem(
    private val uiModel: StoryUiModel
) : BindableItem<ListItemStoryBinding>(uiModel.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.list_item_story

    override fun bind(viewBinding: ListItemStoryBinding, position: Int) {
        viewBinding.uiModel = uiModel
    }
}

data class StoryUiModel(
    val title: String,
    val text: String?,
    val score: Int,
    val user: String,
    val timeAgo: String
)
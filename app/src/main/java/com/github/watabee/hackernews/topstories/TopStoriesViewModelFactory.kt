package com.github.watabee.hackernews.topstories

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateVMFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TopStoriesViewModelFactory @Inject constructor(
    activity: AppCompatActivity,
    private val factory: TopStoriesViewModel.Factory
) : AbstractSavedStateVMFactory(activity, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (!modelClass.isAssignableFrom(TopStoriesViewModel::class.java)) {
            throw IllegalArgumentException("$modelClass isn't assingnable from TopStoriesViewModel")
        }

        @Suppress("UNCHECKED_CAST")
        return factory.create(handle) as T
    }
}
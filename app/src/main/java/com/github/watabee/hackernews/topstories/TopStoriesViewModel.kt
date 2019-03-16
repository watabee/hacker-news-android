package com.github.watabee.hackernews.topstories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.watabee.hackernews.AppRxSchedulers
import com.github.watabee.hackernews.api.HackerNewsApi
import com.github.watabee.hackernews.api.data.HackerNewsItem
import com.github.watabee.hackernews.common.StoryUiModel
import com.github.watabee.hackernews.db.dao.StoryDao
import com.github.watabee.hackernews.db.entity.Story
import com.github.watabee.hackernews.util.ResourceResolver
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class TopStoriesViewModel @Inject constructor(
    private val api: HackerNewsApi,
    private val storyDao: StoryDao,
    private val schedulers: AppRxSchedulers,
    private val resourceResolver: ResourceResolver
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _stories = MutableLiveData<List<StoryUiModel>>()
    val stories: LiveData<List<StoryUiModel>> = _stories

    init {
        findTopStories()
    }

    private fun findTopStories() {
        api.findTopStories()
            .map { it.take(30) }
            .flatMap { storyIds ->
                val now = System.currentTimeMillis()
                Flowable.fromIterable(storyIds)
                    .flatMapSingle({ storyId ->
                        Flowable.concat(
                            storyDao.findStoryById(storyId)
                                .map(this::mapToHackerNewsItem)
                                .toFlowable(),
                            api.findItem(storyId)
                                .flatMap { item ->
                                    storyDao.insertStory(mapToStory(item)).toSingleDefault(item)
                                }
                                .toFlowable()
                        ).firstOrError()
                    }, false, 4)
                    .toList()
                    .map { stories -> stories.sortedBy { storyIds.indexOf(it.id) } }
                    .map { stories -> stories.map { mapToUiModel(it, now) } }
            }
            .observeOn(schedulers.main)
            .subscribeBy(
                onSuccess = _stories::setValue,
                onError = Timber::e
            )
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun mapToUiModel(item: HackerNewsItem, now: Long): StoryUiModel {
        return StoryUiModel(
            title = item.title, text = item.text, score = item.score, user = item.by,
            timeAgo = resourceResolver.getRelativeTimeSpanString(item.time * 1000, now)
        )
    }

    private fun mapToHackerNewsItem(story: Story): HackerNewsItem {
        return HackerNewsItem(
            id = story.id, by = story.by, time = story.time, title = story.title,
            url = story.url, text = story.text, score = story.score
        )
    }

    private fun mapToStory(item: HackerNewsItem): Story {
        return Story(
            id = item.id, by = item.by, time = item.time, title = item.title, url = item.url,
            text = item.text, score = item.score, updatedAt = System.currentTimeMillis()
        )
    }
}
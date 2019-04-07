package com.github.watabee.hackernews.topstories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.watabee.hackernews.AppRxSchedulers
import com.github.watabee.hackernews.api.HackerNewsApi
import com.github.watabee.hackernews.api.data.HackerNewsItem
import com.github.watabee.hackernews.common.StoryUiModel
import com.github.watabee.hackernews.db.dao.StoryDao
import com.github.watabee.hackernews.db.entity.Story
import com.github.watabee.hackernews.topstories.TopStoriesAction.FindTopStoriesAction
import com.github.watabee.hackernews.topstories.TopStoriesEvent.InitialEvent
import com.github.watabee.hackernews.topstories.TopStoriesEvent.RefreshEvent
import com.github.watabee.hackernews.util.ResourceResolver
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Flowable
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject

class TopStoriesViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val api: HackerNewsApi,
    private val storyDao: StoryDao,
    private val schedulers: AppRxSchedulers,
    private val resourceResolver: ResourceResolver
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(handle: SavedStateHandle): TopStoriesViewModel
    }

    private val disposable = CompositeDisposable()

    private val _state = MutableLiveData<TopStoriesState>()
    val state: LiveData<TopStoriesState> = _state

    val loading: LiveData<Boolean> = state.map { it.inProgress }

    private val event = BehaviorSubject.createDefault<TopStoriesEvent>(InitialEvent)

    private val findStoryProcessor = SingleTransformer<List<Long>, List<StoryUiModel>> {
        it.flatMap { storyIds ->
            val now = System.currentTimeMillis()
            Flowable.fromIterable(storyIds)
                .flatMapSingle({ storyId ->
                    Flowable.concat(
                        storyDao.findStoryById(storyId).map(this::mapToHackerNewsItem).toFlowable(),
                        api.findItem(storyId)
                            .flatMap { item ->
                                storyDao.insertStory(mapToStory(item)).toSingleDefault(item)
                            }
                            .toFlowable()
                    ).firstOrError()
                }, false, 4)
                .toList()
                .map { items -> items.sortedBy { item -> storyIds.indexOf(item.id) } }
                .map { items -> items.map { item -> mapToUiModel(item, now) } }
        }
    }

    private val actionProcessor =
        ObservableTransformer<TopStoriesAction, TopStoriesResult> { actions ->
            actions.switchMap {
                api.findTopStories()
                    .map { it.take(30) }
                    .compose(findStoryProcessor)
                    .toObservable()
                    .map<TopStoriesResult> { TopStoriesResult.Success(it) }
                    .onErrorReturn { TopStoriesResult.Failure }
                    .observeOn(schedulers.main)
                    .startWith(TopStoriesResult.InFlight)
            }
        }

    private val reducer =
        BiFunction<TopStoriesState, TopStoriesResult, TopStoriesState> { previousState, result ->
            when (result) {
                is TopStoriesResult.Success ->
                    previousState.copy(topStories = result.topStories, inProgress = false)
                TopStoriesResult.Failure ->
                    previousState.copy(error = true, inProgress = false)
                TopStoriesResult.InFlight ->
                    previousState.copy(error = false, inProgress = true)
            }
        }

    init {
        event.map(this::actionFromEvent)
            .compose(actionProcessor)
            .scan(TopStoriesState.idle(), reducer)
            .subscribeBy(onNext = _state::postValue)
            .addTo(disposable)
    }

    fun refresh() = event.onNext(RefreshEvent)

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun actionFromEvent(event: TopStoriesEvent): TopStoriesAction = when (event) {
        InitialEvent -> FindTopStoriesAction
        RefreshEvent -> FindTopStoriesAction
    }

    private fun mapToUiModel(item: HackerNewsItem, now: Long): StoryUiModel {
        return StoryUiModel(
            title = item.title, text = item.text, score = item.score, user = item.by,
            timeAgo = resourceResolver.getRelativeTimeSpanString(item.time * 1000, now),
            url = item.url
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

private sealed class TopStoriesEvent {

    object InitialEvent : TopStoriesEvent()

    object RefreshEvent : TopStoriesEvent()
}

private sealed class TopStoriesAction {
    object FindTopStoriesAction : TopStoriesAction()
}

private sealed class TopStoriesResult {
    class Success(val topStories: List<StoryUiModel>) : TopStoriesResult()
    object Failure : TopStoriesResult()
    object InFlight : TopStoriesResult()
}
package com.github.watabee.hackernews.topstories

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.github.watabee.hackernews.R
import com.github.watabee.hackernews.common.StoriesAdapter
import com.github.watabee.hackernews.common.StoryBindableItem
import com.github.watabee.hackernews.databinding.ActivityTopStoriesBinding
import com.github.watabee.hackernews.di.ActivityComponent
import com.github.watabee.hackernews.util.bindView
import com.github.watabee.hackernews.util.observeNonNull
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import javax.inject.Inject

class TopStoriesActivity : AppCompatActivity(R.layout.activity_top_stories) {

    @Inject lateinit var viewModelFactory: TopStoriesViewModelFactory

    private val viewModel by viewModels<TopStoriesViewModel> { viewModelFactory }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { StoriesAdapter() }

    private val snackbar by lazy(LazyThreadSafetyMode.NONE) {
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.top_stories_error_message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) { viewModel.refresh() }
    }

    private val customTabsIntent: CustomTabsIntent by lazy(LazyThreadSafetyMode.NONE) {
        CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComponent.create(this).inject(this)
        val binding: ActivityTopStoriesBinding = bindView()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter

        viewModel.state.observeNonNull(this) { state ->
            adapter.update(state.topStories)

            if (state.error) {
                if (!snackbar.isShown) {
                    snackbar.show()
                }
            } else {
                if (snackbar.isShown) {
                    snackbar.dismiss()
                }
            }
        }

        adapter.setOnItemClickListener { item, _ ->
            val uri: Uri = (item as? StoryBindableItem)?.uiModel?.url?.let(Uri::parse)
                ?: return@setOnItemClickListener
            customTabsIntent.launchUrl(this, uri)
        }
    }
}

@AssistedModule
@Module(includes = [AssistedInject_TopStoriesAssistedInjectModule::class])
abstract class TopStoriesAssistedInjectModule

package com.github.watabee.hackernews.topstories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.watabee.hackernews.R
import com.github.watabee.hackernews.common.StoriesAdapter
import com.github.watabee.hackernews.databinding.ActivityTopStoriesBinding
import com.github.watabee.hackernews.di.ActivityComponent
import com.github.watabee.hackernews.di.ViewModelKey
import com.github.watabee.hackernews.util.observeNonNull
import com.google.android.material.snackbar.Snackbar
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject

class TopStoriesActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TopStoriesViewModel> { viewModelFactory }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { StoriesAdapter() }

    private val snackbar by lazy(LazyThreadSafetyMode.NONE) {
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.top_stories_error_message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) { viewModel.refresh() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComponent.create(this).inject(this)
        val binding: ActivityTopStoriesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_top_stories)

        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter

        viewModel.stories.observeNonNull(this, adapter::update)
        viewModel.error.observeNonNull(this) { error ->
            if (error) {
                if (!snackbar.isShown) {
                    snackbar.show()
                }
            } else {
                if (snackbar.isShown) {
                    snackbar.dismiss()
                }
            }
        }
    }
}

@Module
abstract class TopStoriesActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopStoriesViewModel::class)
    abstract fun bindViewModel(viewModel: TopStoriesViewModel): ViewModel
}
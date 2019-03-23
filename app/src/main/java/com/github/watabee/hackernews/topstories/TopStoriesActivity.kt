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
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject

class TopStoriesActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TopStoriesViewModel> { viewModelFactory }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { StoriesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComponent.create(this).inject(this)
        val binding: ActivityTopStoriesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_top_stories)

        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter

        viewModel.stories.observeNonNull(this, adapter::update)
    }
}

@Module
abstract class TopStoriesActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopStoriesViewModel::class)
    abstract fun bindViewModel(viewModel: TopStoriesViewModel): ViewModel
}
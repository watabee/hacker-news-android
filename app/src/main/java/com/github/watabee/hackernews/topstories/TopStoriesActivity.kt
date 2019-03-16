package com.github.watabee.hackernews.topstories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.watabee.hackernews.R
import com.github.watabee.hackernews.common.StoriesAdapter
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
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter

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
package com.github.watabee.hackernews.topstories

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import com.github.watabee.hackernews.R
import com.github.watabee.hackernews.common.StoriesAdapter
import com.github.watabee.hackernews.common.StoryBindableItem
import com.github.watabee.hackernews.databinding.FragmentTopStoriesBinding
import com.github.watabee.hackernews.util.bindView
import com.github.watabee.hackernews.util.dagger.DaggerFragment
import com.github.watabee.hackernews.util.observeNonNull
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class TopStoriesFragment : DaggerFragment(R.layout.fragment_top_stories) {

    @Inject lateinit var viewModelFactory: TopStoriesViewModelFactory

    private val viewModel by viewModels<TopStoriesViewModel> { viewModelFactory }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { StoriesAdapter() }

    private val snackbar by lazy(LazyThreadSafetyMode.NONE) {
        Snackbar.make(
            view!!,
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

        adapter.setOnItemClickListener { item, view ->
            val uri: Uri = (item as? StoryBindableItem)?.uiModel?.url?.let(Uri::parse)
                ?: return@setOnItemClickListener
            customTabsIntent.launchUrl(view.context, uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView<FragmentTopStoriesBinding>().also { binding ->
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.recyclerView.adapter = adapter
        }

        viewModel.state.observeNonNull(viewLifecycleOwner) { state ->
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
    }

    companion object {
        fun newInstance(): TopStoriesFragment = TopStoriesFragment()
    }
}
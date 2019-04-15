package com.github.watabee.hackernews.topstories

import android.os.Bundle
import com.github.watabee.hackernews.util.dagger.DaggerAppCompatActivity

class TopStoriesActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, TopStoriesFragment.newInstance())
                .commit()
        }
    }
}

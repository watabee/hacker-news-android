package com.github.watabee.hackernews.appinitializers

import android.app.Application
import com.github.watabee.hackernews.AppInitializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) : AppInitializer {

    override fun initialize(application: Application) {
        initializers.forEach { it.initialize(application) }
    }
}
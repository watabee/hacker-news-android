package com.github.watabee.hackernews

import android.app.Application

interface AppInitializer {
    fun initialize(application: Application)
}
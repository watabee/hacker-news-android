package com.github.watabee.hackernews.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline onChanged: (T?) -> Unit) {
    observe(owner, Observer { onChanged(it) })
}

inline fun <T> LiveData<T>.observeNonNull(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
) {
    observe(owner, Observer {
        if (it != null) {
            onChanged(it)
        }
    })
}
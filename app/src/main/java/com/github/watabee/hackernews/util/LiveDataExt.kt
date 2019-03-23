package com.github.watabee.hackernews.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

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

inline fun <X, Y> LiveData<X>.map(crossinline mapFunction: (X) -> Y): LiveData<Y> =
    Transformations.map(this) { mapFunction(it) }
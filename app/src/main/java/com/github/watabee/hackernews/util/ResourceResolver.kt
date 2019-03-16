package com.github.watabee.hackernews.util

import android.content.Context
import android.text.format.DateUtils
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceResolver @Inject constructor(appContext: Context) {

    private val res = appContext.resources

    fun getString(@StringRes resId: Int): String = res.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        res.getString(resId, *formatArgs)

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int): String =
        res.getQuantityString(resId, quantity)

    fun getRelativeTimeSpanString(time: Long, now: Long): String =
        DateUtils.getRelativeTimeSpanString(time, now, 0L).toString()
}
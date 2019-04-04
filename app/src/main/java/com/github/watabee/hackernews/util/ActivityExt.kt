package com.github.watabee.hackernews.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

private fun Activity.getContentView(): View = findViewById<ViewGroup>(android.R.id.content)[0]

fun <T : ViewDataBinding> ComponentActivity.bindView(): T = DataBindingUtil.bind(getContentView())!!

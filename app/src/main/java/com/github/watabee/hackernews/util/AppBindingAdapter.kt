package com.github.watabee.hackernews.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.watabee.hackernews.R

@BindingAdapter("goneIfTextIsNullOrEmpty")
fun goneIfTextIsNullOrEmpty(textView: TextView, text: String?) {
    textView.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter(value = ["score", "user", "timeAgo"], requireAll = true)
fun setInfoTextView(textView: TextView, score: Int, user: String, timeAgo: String) {
    val res = textView.context.resources

    val scoreText = res.getQuantityString(R.plurals.score, score, score)
    val userText = res.getString(R.string.story_info_user, user)

    textView.text = "$scoreText $userText ${timeAgo.decapitalize()}"
}

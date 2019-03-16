package com.github.watabee.hackernews.api.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HackerNewsItem(
    val id: Long,
    val by: String,
    val time: Long,
    val title: String,
    val url: String?,
    val text: String?,
    val score: Int
)
package com.github.watabee.hackernews.api

import com.github.watabee.hackernews.api.data.HackerNewsItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApi {

    @GET("/v0/topstories.json")
    fun findTopStories(): Single<List<Long>>

    @GET("/v0/beststories.json")
    fun findBestStories(): Single<List<Long>>

    @GET("/v0/newstories.json")
    fun findNewStories(): Single<List<Long>>

    @GET("/v0/item/{id}.json")
    fun findItem(@Path("id") id: Long): Single<HackerNewsItem>
}
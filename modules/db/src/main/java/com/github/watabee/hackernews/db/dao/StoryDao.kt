package com.github.watabee.hackernews.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.watabee.hackernews.db.entity.Story
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories WHERE id = :id")
    fun findStoryById(id: Long): Maybe<Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: Story): Completable

    @Query("DELETE FROM stories WHERE id IN (:storyIds)")
    fun deleteStories(storyIds: List<Long>): Completable
}
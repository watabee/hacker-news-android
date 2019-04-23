package com.github.watabee.hackernews.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.watabee.hackernews.db.entity.FavoriteStory
import com.github.watabee.hackernews.db.entity.Story
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories")
    fun findStories(): Flowable<List<Story>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun findStoryById(id: Long): Maybe<Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: Story): Completable

    @Query(
        """
        DELETE FROM stories
        WHERE updatedAt < :time
        AND id NOT IN (SELECT storyId FROM favorite_stories)
        """
    )
    fun deleteStories(time: Long)

    @Query("SELECT * FROM stories INNER JOIN favorite_stories ON stories.id = favorite_stories.storyId")
    fun findFavoriteStories(): Flowable<List<Story>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteStory(favoriteStory: FavoriteStory): Completable

    @Query("DELETE FROM favorite_stories WHERE storyId = :storyId")
    fun deleteFavoriteStory(storyId: Long)
}
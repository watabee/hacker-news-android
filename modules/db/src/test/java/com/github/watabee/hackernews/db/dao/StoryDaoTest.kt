package com.github.watabee.hackernews.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.watabee.hackernews.db.AppDatabase
import com.github.watabee.hackernews.db.entity.FavoriteStory
import com.github.watabee.hackernews.db.entity.Story
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class StoryDaoTest {

    @Rule @JvmField val rule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var storyDao: StoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

        storyDao = database.storyDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun testDeleteStories() {
        (1..10L).map(this::makeStory).forEach { storyDao.insertStory(it).test().assertComplete() }

        storyDao.insertFavoriteStory(FavoriteStory(storyId = 1)).test().assertComplete()
        storyDao.insertFavoriteStory(FavoriteStory(storyId = 5)).test().assertComplete()
        storyDao.insertFavoriteStory(FavoriteStory(storyId = 9)).test().assertComplete()

        storyDao.deleteStories(5000L)

        storyDao.findStories()
            .map { stories -> stories.map { it.id } }
            .test()
            .assertValue(listOf(1L, 5L, 6L, 7L, 8L, 9L, 10L))

        storyDao.deleteStories(9000L)

        storyDao.findStories()
            .map { stories -> stories.map { it.id } }
            .test()
            .assertValue(listOf(1L, 5L, 9L, 10L))
    }

    private fun makeStory(id: Long) = Story(
        id = id,
        by = "",
        time = Date().time,
        title = "title",
        url = "",
        text = "",
        score = 1,
        updatedAt = 1000 * id
    )
}
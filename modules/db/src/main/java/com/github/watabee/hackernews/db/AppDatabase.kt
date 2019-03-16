package com.github.watabee.hackernews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.watabee.hackernews.db.dao.StoryDao
import com.github.watabee.hackernews.db.entity.Story

@Database(
    entities = [
        Story::class
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "hacker_news.db")
                    .build()
            }
    }
}
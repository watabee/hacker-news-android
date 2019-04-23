package com.github.watabee.hackernews.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.watabee.hackernews.db.dao.StoryDao
import com.github.watabee.hackernews.db.entity.FavoriteStory
import com.github.watabee.hackernews.db.entity.Story

@Database(
    entities = [
        Story::class,
        FavoriteStory::class
    ],
    version = 2
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "hacker_news.db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }

        @VisibleForTesting
        internal val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `favorite_stories` (`storyId` INTEGER NOT NULL, PRIMARY KEY(`storyId`), FOREIGN KEY(`storyId`) REFERENCES `stories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            }
        }
    }
}
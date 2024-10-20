package com.nalldev.asry.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nalldev.asry.data.datasource.local.models.StoryEntity

@Database(entities = [StoryEntity::class], version = 1)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
}
package com.nalldev.asry.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nalldev.asry.data.datasource.local.dao.RemoteKeysDao
import com.nalldev.asry.data.datasource.local.dao.StoryDao
import com.nalldev.asry.data.datasource.local.models.RemoteKeys
import com.nalldev.asry.data.datasource.local.models.StoryEntity

@Database(entities = [StoryEntity::class, RemoteKeys::class], version = 1)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao() : RemoteKeysDao
}
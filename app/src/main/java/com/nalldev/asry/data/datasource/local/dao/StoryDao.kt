package com.nalldev.asry.data.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nalldev.asry.data.datasource.local.models.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putStories(stories: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    fun removeStories()
}
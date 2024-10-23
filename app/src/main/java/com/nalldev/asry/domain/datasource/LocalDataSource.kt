package com.nalldev.asry.domain.datasource

import androidx.paging.PagingSource
import com.nalldev.asry.data.datasource.local.models.RemoteKeys
import com.nalldev.asry.data.datasource.local.models.StoryEntity

interface LocalDataSource {
    fun putStories(stories: List<StoryEntity>)
    fun getStories(): PagingSource<Int, StoryEntity>
    fun removeStories()

    fun putRemoteKeys(remoteKeys: List<RemoteKeys>)
    fun getRemoteKeysId(id: String): RemoteKeys?
    fun removeRemoteKeys()
}
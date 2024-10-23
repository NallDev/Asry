package com.nalldev.asry.data.datasource.local

import androidx.paging.PagingSource
import com.nalldev.asry.data.datasource.local.dao.RemoteKeysDao
import com.nalldev.asry.data.datasource.local.dao.StoryDao
import com.nalldev.asry.data.datasource.local.models.RemoteKeys
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.domain.datasource.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao
) : LocalDataSource {
    override fun putStories(stories: List<StoryEntity>) {
        return storyDao.putStories(stories)
    }

    override fun getStories(): PagingSource<Int, StoryEntity> {
        return storyDao.getStories()
    }

    override fun removeStories() {
        return storyDao.removeStories()
    }

    override fun putRemoteKeys(remoteKeys: List<RemoteKeys>){
        return remoteKeysDao.putRemoteKeys(remoteKeys)
    }

    override fun getRemoteKeysId(id: String): RemoteKeys? {
        return remoteKeysDao.getRemoteKeysId(id)
    }

    override fun removeRemoteKeys() {
        return remoteKeysDao.removeRemoteKeys()
    }
}
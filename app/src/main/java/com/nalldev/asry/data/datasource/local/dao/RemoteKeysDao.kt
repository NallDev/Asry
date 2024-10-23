package com.nalldev.asry.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nalldev.asry.data.datasource.local.models.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putRemoteKeys(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("SELECT * FROM remote_keys")
    fun getRemoteKeys(): List<RemoteKeys>

    @Query("DELETE FROM remote_keys")
    fun removeRemoteKeys()
}
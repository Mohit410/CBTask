package com.mohitsharda.cbtask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohitsharda.cbtask.model.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postList: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id =:id")
    fun getAllRemoteKeys(id: Int): RemoteKeys

    @Query("DELETE FROM remote_keys")
    fun clearAll()
}
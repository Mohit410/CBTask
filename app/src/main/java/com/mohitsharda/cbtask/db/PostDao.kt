package com.mohitsharda.cbtask.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohitsharda.cbtask.model.PostModel

@Dao
interface PostDao {
    @Query("SELECT * FROM post_table")
    fun getAllPost(): PagingSource<Int,PostModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPost(postList: List<PostModel>)

    @Query("DELETE FROM post_table")
    fun deleteAllPost()
}
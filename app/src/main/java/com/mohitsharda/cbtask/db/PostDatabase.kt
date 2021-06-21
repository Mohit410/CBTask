package com.mohitsharda.cbtask.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohitsharda.cbtask.model.PostModel
import com.mohitsharda.cbtask.model.RemoteKeys

@Database(entities = [PostModel::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    abstract fun getPostDao(): PostDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

}
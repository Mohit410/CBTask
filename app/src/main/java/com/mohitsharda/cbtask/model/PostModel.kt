package com.mohitsharda.cbtask.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "post_table")
data class PostModel(
    @Json(name = "userId") val userId: Int,
    @PrimaryKey @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "body") val body: String
)

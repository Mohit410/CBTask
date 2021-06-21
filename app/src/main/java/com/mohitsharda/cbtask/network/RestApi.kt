package com.mohitsharda.cbtask.network

import com.mohitsharda.cbtask.model.PostModel
import retrofit2.http.GET

interface RestApi {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    @GET("posts")
    suspend fun getAllPost(): List<PostModel>
}
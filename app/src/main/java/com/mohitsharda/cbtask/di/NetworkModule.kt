package com.mohitsharda.cbtask.di

import android.app.Application
import androidx.room.Room
import com.mohitsharda.cbtask.db.PostDatabase
import com.mohitsharda.cbtask.network.RestApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()!!

    @Provides
    @Singleton
    fun providesRestApi(moshi: Moshi) = Retrofit.Builder()
        .baseUrl(RestApi.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(RestApi::class.java)!!

    @Provides
    @Singleton
    fun providesDatabase(context: Application) =
        Room.databaseBuilder(context, PostDatabase::class.java, "posts_database").build()

    @Provides
    @Singleton
    fun providesPostDao(db: PostDatabase) = db.getPostDao()

    @Provides
    @Singleton
    fun providesRemoteKeyDao(db: PostDatabase) = db.getRemoteKeysDao()
}
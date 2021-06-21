package com.mohitsharda.cbtask.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mohitsharda.cbtask.db.PostDatabase
import com.mohitsharda.cbtask.model.PostModel
import com.mohitsharda.cbtask.model.RemoteKeys
import com.mohitsharda.cbtask.network.RestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
class PostRemoteMediator constructor(private val db: PostDatabase, private val restApi: RestApi) :
    RemoteMediator<Int, PostModel>() {

    private val index = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostModel>
    ): MediatorResult {
        val page = when (val pageData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageData
            else -> pageData as Int
        }

        try {
            val response = restApi.getAllPost()

            val isEndOfList = response.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getPostDao().deleteAllPost()
                    db.getRemoteKeysDao().clearAll()
                }
                val prevKey = if (page == index) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1

                val keys = response.map {
                    RemoteKeys(it.id, prevKey, nextKey)
                }

                db.getRemoteKeysDao().insertAll(keys)
                db.getPostDao().insertAllPost(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, PostModel>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRefreshRemoteKey(state)
                remoteKey?.nextKey?.minus(1) ?: index
            }
            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(state)
                remoteKey?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                remoteKey?.nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, PostModel>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let { post ->
                    db.getRemoteKeysDao().getAllRemoteKeys(post.id)
                }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, PostModel>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let { post ->
                    db.getRemoteKeysDao().getAllRemoteKeys(id = post.id)
                }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, PostModel>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { it ->
                state.closestItemToPosition(it)?.id?.let { id ->
                    db.getRemoteKeysDao().getAllRemoteKeys(id)
                }
            }
        }
    }

}










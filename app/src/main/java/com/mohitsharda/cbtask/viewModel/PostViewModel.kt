package com.mohitsharda.cbtask.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mohitsharda.cbtask.db.PostDatabase
import com.mohitsharda.cbtask.model.PostModel
import com.mohitsharda.cbtask.network.RestApi
import com.mohitsharda.cbtask.repository.PostRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val db: PostDatabase,
    private val restApi: RestApi
) : ViewModel() {

    @ExperimentalPagingApi
    fun getAllPost(): Flow<PagingData<PostModel>> = Pager(
        config = PagingConfig(10, enablePlaceholders = false),
        remoteMediator = PostRemoteMediator(db, restApi)
    ) {
        db.getPostDao().getAllPost()
    }.flow.cachedIn(viewModelScope)

}
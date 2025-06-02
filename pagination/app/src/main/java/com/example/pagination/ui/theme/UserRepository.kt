package com.example.pagination.ui.theme

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class UserRepository {
    // Returns paging flow with dynamic page size
    fun getUsersPagingFlow(pageSize: Int = 15): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPagingSource() }
        ).flow
    }
}
package com.example.pagination.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    // Current page size state
    var currentPageSize by mutableStateOf(10)
        private set

    // StateFlow to trigger pagination refresh when page size changes
    private val pageSizeFlow = MutableStateFlow(currentPageSize)

    // Users flow that rebuilds when page size changes
    val users: Flow<PagingData<User>> = pageSizeFlow.flatMapLatest { pageSize ->
        repository.getUsersPagingFlow(pageSize)
    }.cachedIn(viewModelScope)

    // Function to update page size
    fun updatePageSize(newPageSize: Int) {
        currentPageSize = newPageSize
        pageSizeFlow.value = newPageSize
    }
}
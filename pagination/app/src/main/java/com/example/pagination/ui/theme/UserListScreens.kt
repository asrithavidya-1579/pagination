package com.example.pagination.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserViewModel = viewModel(),
    onUserClick: (Int) -> Unit = {}
) {
    val users = viewModel.users.collectAsLazyPagingItems()
    val pageSize = viewModel.currentPageSize ///
    var showDialog by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val itemHeight = (screenHeight * 0.1f).dp // 10% of screen height


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        "Team Members",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color(0xFFF8F9FA))
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(minOf(users.itemCount, viewModel.currentPageSize)) { index -> ///
                        val user = users[index]
                        user?.let {
                            UserCard(
                                index = index,
                                user = it,
                                onClick = { onUserClick(it.id) }
                            )
                        }
                    }

                    when (users.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    message = "Failed to load more items",
                                    onRetry = { users.retry() }
                                )
                            }
                        }
                        else -> {}
                    }
                }

                // Handle initial loading state
                if (users.loadState.refresh is LoadState.Loading && users.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF667eea),
                            strokeWidth = 3.dp
                        )
                    }
                }

                // Handle initial error state
                if (users.loadState.refresh is LoadState.Error && users.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorItem(
                            message = "Failed to load data",
                            onRetry = { users.refresh() }
                        )
                    }
                }
            }
        }

        // Floating Action Button for pagination settings (positioned at bottom right)
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF667eea),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Pagination Settings"
            )
        }
    }

    // Pagination Settings Dialog
    if (showDialog) {
        PaginationDialog(
            currentPageSize = viewModel.currentPageSize,
            onPageSizeSelected = { pageSize ->
                viewModel.updatePageSize(pageSize)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun PaginationDialog(
    currentPageSize: Int,
    onPageSizeSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val pageSizes = listOf(5, 10, 15, 20)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Items per page",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        },
        text = {
            Column {
                Text(
                    text = "Choose how many items to display per page:",
                    color = Color(0xFF7F8C8D),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Pagination size buttons
                pageSizes.chunked(2).forEach { rowSizes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSizes.forEach { size ->
                            Button(
                                onClick = { onPageSizeSelected(size) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (size == currentPageSize)
                                        Color(0xFF667eea) else Color(0xFFE3F2FD),
                                    contentColor = if (size == currentPageSize)
                                        Color.White else Color(0xFF667eea)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$size items",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    "Close",
                    color = Color(0xFF667eea),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun UserCard(index: Int, user: User, onClick: () -> Unit = {}) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val itemHeight = (screenHeight * 0.1f).dp // 10% of screen height
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .height(itemHeight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            AsyncImage(
                model = user.avatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = Color(0xFF7F8C8D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF667eea).copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = user.role,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF667eea),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Joined ${user.joinDate}",
                        fontSize = 11.sp,
                        color = Color(0xFF95A5A6)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF667eea),
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun ErrorItem(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color(0xFFE74C3C),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF667eea)
            )
        ) {
            Text("Retry", color = Color.White)
        }
    }
}
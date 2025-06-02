package com.example.pagination.ui.theme

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay

class UserPagingSource : PagingSource<Int, User>() {

    private val TAG="PagingSource.kt"
    // All data stored in memory - no database or network calls
    private val dummyUsers = generateDummyUsers()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        Log.d("DDDDDD", "pageNo = ${params.key}")
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            // Simulate loading delay (like a real API call)
            delay(800)
            Log.d("DDDDDD", "pageNo1 = ${params.key}")
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, dummyUsers.size)

            val users = if (startIndex < dummyUsers.size) {
                dummyUsers.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            LoadResult.Page(
                data = users,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (endIndex >= dummyUsers.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, User>): Int? {



        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
                        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    // Generate completely local dummy data - no external dependencies
    private fun generateDummyUsers(): List<User> {
        val names = listOf(
            "Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince", "Ethan Hunt",
            "Fiona Green", "George Wilson", "Hannah Davis", "Ian Clarke", "Julia Roberts",
            "Kevin Hart", "Luna Lovegood", "Mike Tyson", "Nina Williams", "Oscar Wild",
            "Paula Abdul", "Quinn Fabray", "Rachel Green", "Steve Jobs", "Tina Turner",
            "Uma Thurman", "Victor Hugo", "Wendy Williams", "Xavier Charles", "Yara Shahidi",
            "Zoe Saldana", "Adam Lambert", "Betty White", "Carl Jung", "Dolly Parton",
            "Elvis Presley", "Frank Sinatra", "Grace Kelly", "Henry Ford", "Iris West",
            "Jack Black", "Kate Winslet", "Liam Neeson", "Maya Angelou", "Neil Armstrong",
            "Oprah Winfrey", "Paul McCartney", "Queen Latifah", "Robert Downey", "Sarah Connor",
            "Tom Hanks", "Ursula Le Guin", "Viola Davis", "Will Smith", "Xena Warrior",
            "Yoda Master", "Zendaya Coleman", "Abraham Lincoln", "Beyonce Knowles", "Chris Evans"
        )

        val roles = listOf("Developer", "Designer", "Manager", "Analyst", "Engineer", "Consultant")
        val domains = listOf("@gmail.com", "@yahoo.com", "@outlook.com", "@company.com")

        return names.mapIndexed { index, name ->
            User(
                id = index + 1,
                name = name,
                email = "${name.lowercase().replace(" ", ".")}${domains.random()}",
                avatar = "https://i.pravatar.cc/150?img=${index + 1}",
                role = roles.random(),
                joinDate = "202${(0..4).random()}-${(1..12).random().toString().padStart(2, '0')}-${(1..28).random().toString().padStart(2, '0')}"
            )
        }
    }
}

package com.example.pagination.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    onBackClick: () -> Unit
) {
    // Find user by ID from dummy data (in a real app, this would come from repository)
    val user = remember {
        generateDummyUsers().find { it.id == userId }
    }

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
                    text = "User Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
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
            if (user != null) {
                UserDetailContent(user = user)
            } else {
                // User not found
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "User not found",
                        fontSize = 18.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetailContent(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Avatar
        AsyncImage(
            model = user.avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // User Name
        Text(
            text = user.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Role Badge
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF667eea).copy(alpha = 0.1f)
            )
        ) {
            Text(
                text = user.role,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF667eea),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // User Details Cards
        UserDetailItem(
            icon = Icons.Default.Email,
            label = "Email",
            value = user.email
        )

        Spacer(modifier = Modifier.height(16.dp))

        UserDetailItem(
            icon = Icons.Default.Person,
            label = "User ID",
            value = "#${user.id.toString().padStart(3, '0')}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        UserDetailItem(
            icon = Icons.Default.DateRange,
            label = "Join Date",
            value = user.joinDate
        )
    }
}

@Composable
fun UserDetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF667eea),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color(0xFF7F8C8D),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Helper function to generate dummy users (same as in PagingSource)
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
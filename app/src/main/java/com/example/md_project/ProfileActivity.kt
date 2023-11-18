package com.example.md_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.md_project.ui.theme.Book
import com.example.md_project.ui.theme.BookViewModel

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ProfilePage(navController = rememberNavController(), bookViewModel = BookViewModel())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavController, bookViewModel: BookViewModel) {
    // List of books for each category
    val toReadBooks by remember { bookViewModel.toReadBooks }
    val readingBooks by remember { bookViewModel.readingBooks }
    val readBooks by remember { bookViewModel.readBooks }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar and back button in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) // Adjust the height
        ) {
            // Search bar
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp), // Round the corners
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent, // Remove the underline
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = LocalContentColor.current
                ),
                placeholder = {
                    Text("Search", color = Color.Gray) // Use placeholder instead of label
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )

            // Button with back icon to the main activity
            IconButton(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BookCategoryRow(
            categoryTitle = "To Read",
            books = toReadBooks,
            onBookClick = { book -> bookViewModel.addToToRead(book) }
        )

        BookCategoryRow(
            categoryTitle = "Reading",
            books = readingBooks,
            onBookClick = { book -> bookViewModel.addToReading(book) }
        )

        BookCategoryRow(
            categoryTitle = "Read",
            books = readBooks,
            onBookClick = { book -> bookViewModel.addToRead(book) }
        )



        // Your existing code...
    }
}

@Composable
fun BookCategoryRow(
    categoryTitle: String,
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = categoryTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(books) { book ->
                BookItem(book = book, onBookClick = onBookClick)
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onBookClick: (Book) -> Unit) {
    // You can customize the appearance of each book item here
    // For simplicity, just displaying the book title for now
    Text(
        text = book.title,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onBookClick(book) }
    )
    }


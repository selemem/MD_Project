package com.example.md_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.md_project.ui.theme.Book
import com.example.md_project.ui.theme.BookViewModel
import com.example.md_project.ui.theme.readBooksFromAssets

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

    // Read book information from the books.txt file
    val books = readBooksFromAssets(LocalContext.current, "books.txt")

    var searchText by remember { mutableStateOf("") }


    // Filtered list based on search text
    val filteredBooks = if (searchText.isNotBlank()) {
        books.filter { it.title.contains(searchText, ignoreCase = true) }
    } else {
        books
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar and back button in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Adjust the height
        ) {
            // Search bar
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(50.dp),
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


        // Conditionally display the LazyRow only when searching
        if (searchText.isNotBlank()) {
            // Displaying the filtered books
            if (filteredBooks.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(filteredBooks) { book ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // Navigate to book details activity
                                    navController.navigate("bookDetails/${book.title}")
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(142.dp)
                                    .wrapContentHeight()
                            ) {
                                // Book cover image
                                Image(
                                    painter = rememberImagePainter(
                                        data = LocalContext.current.resources.getIdentifier(
                                            book.cover,
                                            "drawable",
                                            LocalContext.current.packageName
                                        ),
                                        builder = {
                                            crossfade(true)
                                            transformations(RoundedCornersTransformation(8f))
                                        }
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(142.dp) // Square dimensions
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )

                                // Book title with padding
                                Text(
                                    text = book.title,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp) // Adjust the padding as needed
                                        .padding(top = 8.dp),
                                    style = TextStyle.Default.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // Display a message when no books match the search
                Text(
                    text = "No matching books found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    style = TextStyle.Default.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        BookCategoryRow(
            categoryTitle = "To Read",
            books = toReadBooks,
            onBookClick = { book ->
                navController.navigate("bookDetails/${book.title}")
            }
        )

        BookCategoryRow(
            categoryTitle = "Reading",
            books = readingBooks,
            onBookClick = { book ->
                navController.navigate("bookDetails/${book.title}")
            }
        )

        BookCategoryRow(
            categoryTitle = "Read",
            books = readBooks,
            onBookClick = { book ->
                navController.navigate("bookDetails/${book.title}")
            }
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
    Column {
        Text(
            text = categoryTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp, top = 8.dp),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(books) { book ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // Navigate to book details activity
                            onBookClick(book)
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                            .width(142.dp)
                            .wrapContentHeight()
                    ) {
                        // Book cover image
                        Image(
                            painter = rememberImagePainter(
                                data = LocalContext.current.resources.getIdentifier(
                                    book.cover,
                                    "drawable",
                                    LocalContext.current.packageName
                                ),
                                builder = {
                                    crossfade(true)
                                    transformations(RoundedCornersTransformation(8f))
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                        )

                        // Book title with padding
                        Text(
                            text = book.title,
                            modifier = Modifier
                                .padding(horizontal = 8.dp) // Adjust the padding as needed
                                .padding(top = 8.dp),
                            style = TextStyle.Default.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        // Display stars only for books in the "Read" category
                        if (categoryTitle == "Read") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(5) { index ->
                                    val starAlpha = if (index < book.stars) 1f else 0.2f

                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .alpha(starAlpha)
                                    )
                                }
                            }
                        }
                    }
                }
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